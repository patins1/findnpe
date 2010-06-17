package topPackage.attack;

import pingpong.annotations.CanBeNull;
import pingpong.annotations.NonNull;

public class Data {

	public String getS1() {
		return null; /* error1 easy NOATTACK */
	}

	@CanBeNull
	public String getS2() {
		return null; /* OK */
	}

	public void doS1(String param1) {
		param1.toString(); /* OK */
	}

	public void doS2(@CanBeNull String param1) {
		param1.toString(); /* error0 */
	}

	public void doS3(@NonNull String param1) {
		param1.toString(); /* OK */
	}

	public void doS4(String param1) {
		param1.toString(); /* OK */
	}

}
