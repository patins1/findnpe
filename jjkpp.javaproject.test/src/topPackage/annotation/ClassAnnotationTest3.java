package topPackage.annotation;

import pingpong.annotations.CanBeNull;

public class ClassAnnotationTest3 extends ClassAnnotationTest {

	public ClassAnnotationTest3(@CanBeNull Integer s) {
		super(s);/* error1 */
	}

	public ClassAnnotationTest3(String s) {
		super(s);
	}

	protected String testReturnNull() {
		return null; /* error1 easy */
	}

}
