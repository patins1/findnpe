package topPackage.assignment;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;

public class FieldAssignmentTest {

	@NonNull
	String s1 = ""/* OK */;

	@NonNull
	String s4 = null/* error0 */;

	@CanBeNull
	String s5 = null/* OK */;

	FieldAssignmentTest tdefault;

	@NonNull
	FieldAssignmentTest tnn;

	@CanBeNull
	FieldAssignmentTest tcbn;

	private void testNonNullFieldVariableAssignNull() {
		s1 = null/* OK */;
	}

	private void testNonNullThisFieldVariableAssignNull() {
		this.s1 = null/* OK */;
	}

	private void testNonNullFieldVariableAssignNullValue() {
		String s = null;
		s1 = s/* error1 */;
	}

	private void testNonNullThisFieldVariableAssignNullValue() {
		String s = null;
		this.s1 = s/* error1 */;
	}

	private void testNonNullThisFieldVariableAssignUnknownValue() {
		String s = "";
		if (s.equals("x")) {
			s = null;
		}
		this.s1 = s/* error1 */;
	}

	private void testNonNullThisFieldVariableAssignNonNullValue() {
		String s = "";
		this.s1 = s/* OK */;
	}

	private void testNonNullQualifiedNameFieldAssignUnknownValue() {
		FieldAssignmentTest t = new FieldAssignmentTest();
		FieldAssignmentTest x = new FieldAssignmentTest();
		if ("".contains("x"))
			x = null;
		t.tnn.tnn = x; /* error1 */
	}

	private void testDefaultNonNullQualifiedNameReferenceFieldAssignUnknownValue() {
		FieldAssignmentTest t = new FieldAssignmentTest();
		FieldAssignmentTest x = new FieldAssignmentTest();
		if ("".contains("x"))
			x = null;
		t.tnn.tdefault = x; /* error2 NOATTACK */
	}

	private void testCanBeNullQualifiedNameReferenceFieldAssignUnknownValue() {
		FieldAssignmentTest t = new FieldAssignmentTest();
		FieldAssignmentTest x = new FieldAssignmentTest();
		if ("".contains("x"))
			x = null;
		t.tnn.tcbn = x; /* OK */
	}

}