package topPackage.annotation;

public class ClassAnnotationTest3 extends ClassAnnotationTest {

	public ClassAnnotationTest3(String s) {
		super(s);
	}

	protected String testReturnNull() {
		return null; /* error1 easy */
	}

}
