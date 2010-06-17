package topPackage.assignment;

import pingpong.annotations.NonNull;

public class LocalVariableAssignmentTest {

	private void testNonNullLocalVariableAssignNull() {
		@NonNull
		String s1 = "";
		s1 = null/* error0 */;
	}

	private void testNonNullLocalVariableAssignCanBeNullVariableWithNonNullValue() {
		@NonNull
		String s1 = "";
		String s2 = "";
		s1 = s2/* OK */;
	}

	private void testNonNullLocalVariableAssignCanBeNullVariableWithNullValue() {
		@NonNull
		String s1 = "";
		String s2 = null;
		s1 = s2/* error1 */;
	}

	private void testNonNullLocalVariableAssignUnknownValue() {
		@NonNull
		String s1 = "";
		String s2 = "";
		if (s2.equals("x")) {
			s2 = null;
		} else {
			s2 = "y";
		}
		s1 = s2/* error1 */;
	}

	private void testNonNullLocalVariableAssignNonNullValue() {
		@NonNull
		String s1 = "";
		String s2 = "";
		s1 = s2/* OK */;
	}

	private void testNonNullLocalVariableAssignNonNullVariableWithNonNullValue() {
		@NonNull
		String s1 = "";
		@NonNull
		String s2 = "";
		s1 = s2/* OK */;
	}

	private void testNonNullLocalVariableAssignNonNullVariableWithNullValue() {
		@NonNull
		String s1 = "";
		@NonNull
		String s2 = null/* error0 */;
		s1 = s2/* error0 */;
	}

	private void testNonNullLocalVariableInitializeWithNull() {
		@NonNull
		String s1 = null/* error0 */;
	}

}