package topPackage.annotation;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;

public class OverrideAnnotationTest {

	protected void testParamNonNull(@NonNull String s) {
	}

	protected String testParamCanBeNull(@CanBeNull String s) {
		return "";
	}

	@NonNull
	protected String testReturnNonNull() {
		return "";
	}

	@CanBeNull
	protected String testReturnCanBeNull() {
		return "";
	}

	protected String testReturnAndParamError(@CanBeNull String s) {
		return "";
	}

	void test() {
		new OverrideAnnotationTest() {

			@CanBeNull
			/* error0 */
			protected String testReturnNonNull() {
				return "";
			}

			@NonNull
			/* OK */
			protected String testReturnCanBeNull() {
				return "";
			}

			protected void testParamNonNull(@CanBeNull String s) {/* OK */
			}

			@NonNull
			protected String testParamCanBeNull(@NonNull String s) { /* error0 */
				return "";
			}

			@CanBeNull
			/* error0 */
			protected String testReturnAndParamError(@NonNull String s) { /* error0 */
				return "";
			}
		};

	}

	void test2() {
		new OverrideAnnotationTest() {

			@Override
			protected String testParamCanBeNull(String s) {
				testParamCanBeNull(null); /* OK */
				return "";
			}

			@Override
			protected String testReturnCanBeNull() {
				return null; /* OK */
			}
		};

	}
}