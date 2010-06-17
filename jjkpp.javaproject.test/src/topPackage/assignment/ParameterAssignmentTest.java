package topPackage.assignment;

import pingpong.annotations.CanBeNull;
import pingpong.annotations.NonNull;

public class ParameterAssignmentTest {

	public ParameterAssignmentTest(String s1) {
		s1.toString(); /* OK */
		s1 = null/* OK */;
		s1.toString(); /* error1 */
	}

	private void testDefaultParameterVariableAssignNull(String s1) {
		s1.toString(); /* OK */
		s1 = null/* OK */;
		s1.toString(); /* error1 */
	}

	private void testNonNullParameterVariableAssignNull(@NonNull String s1) {
		s1.toString(); /* OK */
		s1 = null/* OK */;
		s1.toString(); /* error0 */
	}

	private void testCanBeNullParameterVariableAssignNull(@CanBeNull String s1) {
		s1.toString(); /* error0 */
		s1 = "";
		s1.toString(); /* OK */
		s1 = null/* OK */;
		s1.toString(); /* error0 */
	}

}