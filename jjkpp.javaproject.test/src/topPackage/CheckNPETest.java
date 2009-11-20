package topPackage;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNull;
import jjkpp.jdt.annotations.NonNullParam2;

public class CheckNPETest {

	CheckNPETest tdefault;

	@NonNull
	CheckNPETest tnn;

	@CanBeNull
	CheckNPETest tcbn;

	@NonNull
	String s1;

	@CanBeNull
	String s2;

	static @NonNull
	String ss1;

	static @CanBeNull
	String ss2;

	private void testQualifiedNameReference() {
		CheckNPETest t = new CheckNPETest();

		CheckNPETest x;
		x = tnn.tcbn/* ok */;
		x = this.tnn.tcbn/* ok */;
		x = t.tcbn/* ok */;
		x = this.tcbn/* ok */;
		x = tcbn/* ok */;

		this.tcbn.tnn.toString(); /* error0 */
		this.tnn.toString(); /* ok */
		this.tnn.tnn.toString(); /* ok */
		this.tnn.tcbn.toString(); /* error0 */

		tcbn.tnn.toString(); /* error0 */
		tnn.toString(); /* ok */
		tnn.tnn.toString(); /* ok */
		tnn.tcbn.toString(); /* error0 */

		t.tcbn.tnn.toString(); /* error0 */
		t.tnn.toString(); /* ok */
		t.tnn.tnn.toString(); /* ok */
		t.tnn.tcbn.tnn.toString(); /* error0 */
		t.tnn.tcbn.toString(); /* error0 */

	}

	@CanBeNull
	@CanBeNullParam1
	@NonNullParam2
	private String testMethod(String x1, String x2) {
		testMethod("", "").toString();/* error0 */
		x1.toString();/* error0 */
		x2.toString();/* ok */
		return null;
	}

	private void testString() {
		new CheckNPETest().s1.toString();/* ok */
		new CheckNPETest().s2.toString();/* error0 */
		new CheckNPETest().ss1.toString();/* ok */
		new CheckNPETest().ss2.toString();/* error0 */
		CheckNPETest.ss1.toString();/* ok */
		CheckNPETest.ss2.toString();/* error0 */
	}

	private void testLocal() {
		CheckNPETest t1 = new CheckNPETest();
		t1.toString();/* ok */

		CheckNPETest t2 = new CheckNPETest();
		if ("".contains("x"))
			t2 = null;
		t2.toString();/* error1 */

		CheckNPETest t3 = null;
		t3.toString();/* error1 */
	}

	void testCompoundStatement() {
		Integer i = null;
		i++; /* error1 */
	}

	void testBinaryExpression() {
		Integer i = null;
		Integer j = i + 8; /* error1 */
	}

}