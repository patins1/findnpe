package topPackage.attack;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNullParam1;

public class Data {

	public String getS1() {
		return null; /* error1 NOATTACK */
	}

	@CanBeNull
	public String getS2() {
		return null; /* OK */
	}

	public void doS1(String param1) {
		param1.toString(); /* OK */
	}

	@CanBeNullParam1
	public void doS2(String param1) {
		param1.toString(); /* error0 */
	}

	@NonNullParam1
	public void doS3(String param1) {
		param1.toString(); /* OK */
	}
	
	public void doS4(String param1) {
		param1.toString(); /* OK */
	}

}
