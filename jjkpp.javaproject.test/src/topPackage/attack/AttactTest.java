package topPackage.attack;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;

public class AttactTest {

	Data d = new Data();

	public String testReturnDefaultReturn() {
		String s = d.getS1();
		s.toString(); /* OK */
//		if (s!=null)
//			s.toString(); /* OK */
		return s;
	}

	public String testReturnNull() {
		return null; /* error1 NOATTACK */
	}

	@NonNull
	public String testReturnNullOnExpectedNonNull() {
		return null; /* error0 easy */
	}


	public void testAccessCanBeNullReturn() {
		String s = d.getS2();
		s.toString(); /* error1 */
	}

	public void testAccessCanBeNullReturn2() {
		String s = new DData().getS2();
		s.toString(); /* error1 */
	}

	public String testReturnCanBeNullReturn() {
		String s = d.getS2();
		return s; /* error1 NOATTACK */
	}
	
	@NonNull
	public String testNonNullReturnCanBeNullReturn() {
		String s = d.getS2();
		return s; /* error1 */
	}

	@CanBeNull
	public String testCanBeNullReturnCanBeNull() {
		String s = d.getS2();
		return s; /* OK */
	}

	public void testDefaultParam() {
		d.doS1(null); /* error1 NOATTACK */ 
	}

	public void testCanBeNullParam() {
		d.doS2(null); /* OK */
	}
	
	public void testNonNullParam() {
		d.doS3(null); /* error0 easy */
	}

}
