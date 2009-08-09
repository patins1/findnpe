package topPackage;


public class DefaultTest {

	String s4=null/*error1*/;
	
	private String testNonNullReturn(String param1) {
		String s=null;
		testNonNullReturn("");/*ok*/
		testNonNullReturn(null);/*error1 easy*/
		testNonNullReturn(s);/*error2*/
		param1.toString();/*OK*/
		return null/*error1 easy*/;
	}
	
}