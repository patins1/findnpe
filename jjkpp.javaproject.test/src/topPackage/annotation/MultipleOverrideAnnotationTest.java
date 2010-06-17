package topPackage.annotation;

import pingpong.annotations.CanBeNull;
import pingpong.annotations.NonNull;

public class MultipleOverrideAnnotationTest {

	@CanBeNull
	protected String testReturn() {
		return "";
	}
}

abstract class MultipleOverrideAnnotationTest2 extends MultipleOverrideAnnotationTest {

	@NonNull
	abstract protected String testReturn();

}

class MultipleOverrideAnnotationTest3 extends MultipleOverrideAnnotationTest2 {

	@Override
	protected String testReturn() {
		return null; /* error0 easy */
	}

}

class MultipleOverrideAnnotationTest4 extends MultipleOverrideAnnotationTest2 {

	@CanBeNull
	/* error0 */
	@Override
	protected String testReturn() {
		return "";

	}

}