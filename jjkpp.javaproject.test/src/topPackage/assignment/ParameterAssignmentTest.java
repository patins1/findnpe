package topPackage.assignment;

import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNullParam1;

public class ParameterAssignmentTest {

	private void testDefaultParameterVariableAssignNull(String s1) {
		s1.toString(); /* OK */;
		s1 = null/* OK */;  
		s1.toString(); /* error1 */;
	}
	
	@NonNullParam1
	private void testNonNullParameterVariableAssignNull(String s1) {
		s1.toString(); /* OK */;      
		s1 = null/* OK */;
		s1.toString(); /* error0 */;
	}

	@CanBeNullParam1
	private void testCanBeNullParameterVariableAssignNull(String s1) {
		s1.toString(); /* error0 */; 
		s1="";
		s1.toString(); /* OK */; 
		s1 = null/* OK */;
		s1.toString(); /* error0 */;
	}

}