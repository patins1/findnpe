package topPackage;

import findnpe.annotations.CanBeNull;
import findnpe.annotations.NonNull;

public class ReturnTest {

	@NonNull
	private String testNonNullReturn() {
		testNonNullReturn().toString();/* OK */
		return null/* error0 easy */;
	}

	@CanBeNull
	private String testNullReturn() {
		testNullReturn().toString();/* error0 */
		return null/* OK */;
	}

}