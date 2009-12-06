package topPackage.annotation;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;
import jjkpp.jdt.annotations.NonNullByDefault;

@NonNullByDefault
public class ClassAnnotationTest {

	String testReturnNull() {
		return null; /* error1 easy */
	}

	void testReturnNull(String param1) {
		testReturnNull(null); /* error1 easy */
	}

}