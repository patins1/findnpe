package topPackage;

import java.util.Random;

import findnpe.annotations.CanBeNull;

public class NullStatusTest {

	@CanBeNull
	String f;

	void testQualifiedNameReference() {
		NullStatusTest t = this;
		t.f.toString();/* error0 */
	}

	void testSingleNameReference() {
		NullStatusTest t = null;
		t.toString();/* error1 */
	}

	void testFieldReference() {
		f.toString();/* error0 */
	}

	@CanBeNull
	String testMessageSend() {
		testMessageSend().toString();/* error0 */
		return null;
	}

	void testArrayAllocationExpression1() {
		String[] x = new String[] { "x", null };
		x[10].toString(); /* ok */
		x.toString(); /* ok */
	}

	void testArrayAllocationExpression2() {
		String[] x = new String[] { "x", null };
		x.toString(); /* ok */
		x[10].toString(); /* ok */
	}

	void testArrayAllocationExpression3() {
		String[] x = null;
		String[] y = new String[] {};
		y[0].toString(); /* ok */
		y = x;
		y[0].toString(); /* error1 */
	}

	void testConditionalExpression2() {
		String s = new Random().nextBoolean() ? "" : null;
		s.toCharArray(); /* error1 */
	}

	void testConditionalExpression3() {
		String s = new Random().nextBoolean() ? "" : "";
		s.toCharArray(); /* ok */
	}

	void testAssignmentExpression() {
		String s = "".toString();
		String ss = (s = null);
		ss.toCharArray(); /* error1 */
	}

	public void testMergeDefNNAndProtNull() {
		String s = "";
		if (s != null)
			"".toCharArray();
		s.hashCode(); /* OK */
	}

	public void testMergeProtNullAndDefNN() {
		String s = "";
		if (s == null)
			;
		else
			"".toCharArray();
		s.hashCode(); /* OK */
	}

}
