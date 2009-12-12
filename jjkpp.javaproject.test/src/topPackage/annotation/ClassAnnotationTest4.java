package topPackage.annotation;

import jjkpp.jdt.annotations.NonNull;

public class ClassAnnotationTest4 extends ClassAnnotationTest {

	public ClassAnnotationTest4(String s) {
		super("");
		s.toString(); /* error0 */
	}

	@NonNull
	protected String testReturnNull() {
		return null; /* error0 easy */
	}

}
