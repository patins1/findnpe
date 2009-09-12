package topPackage.assignment;

import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNullParam1;

public class ParameterAssignmentTest {

	@NonNullParam1
	private void testNonNullParameterVariableAssignNull(String s1) {
		s1 = null/* error0 */;
	}

	@CanBeNullParam1
	private void testCanBeNullParameterVariableAssignNull(String s1) {
		s1 = null/* OK */;
	}

}