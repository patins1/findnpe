package topPackage.annotation;

import pingpong.annotations.CanBeNull;
import pingpong.annotations.NonNull;

public class AnnotationTest {

	/* OK */
	private void testAnnotatedParameters(@NonNull String s1, @CanBeNull String s2) {
	}

	private void testOppositeAnnotationsOnParameter(@CanBeNull @NonNull String s1) {/* error0 */
	}

	@NonNull
	@CanBeNull
	/* error0 */
	private String testOppositeAnnotationsOnReturn(String s1) {
		return "";
	}

	@NonNull
	/* OK */
	private void testReturnAnnotationOnVoid(String s1) {
	}

	@NonNull
	@CanBeNull
	/* error0 */
	private String testDoubleAnnotation(String s1) {
		return "";
	}

}