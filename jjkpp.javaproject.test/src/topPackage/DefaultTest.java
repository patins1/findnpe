package topPackage;

public class DefaultTest {

	String s4 = null/* error1 NOATTACK */;

	private String testNonNullReturn(String param1) {
		String s = null;
		testNonNullReturn("");/* ok */
		testNonNullReturn(null);/* error1 easy NOATTACK */
		testNonNullReturn(s);/* error2 NOATTACK */
		param1.toString();/* OK */
		return null/* error1 easy NOATTACK */;
	}

}