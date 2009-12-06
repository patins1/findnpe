package topPackage.annotation.packagetest;

public class PackageAnnotationTest {

	String testReturnNull() {
		return null; /* error0 easy *//* TODO 1 */
	}

	void testReturnNull(String param1) {
		testReturnNull(null); /* error0 easy *//* TODO 1 */
	}

}