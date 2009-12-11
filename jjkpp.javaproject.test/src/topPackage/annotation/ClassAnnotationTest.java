package topPackage.annotation;

import jjkpp.jdt.annotations.NonNullByDefault;

@NonNullByDefault
public class ClassAnnotationTest {

	protected String testReturnNull() {
		return null; /* error1 easy */
	}

	protected void testReturnNull(String param1) {
		testReturnNull(null); /* error1 easy */
	}

	class ClassAnnotationTest1 {

		protected String testReturnNull() {
			return null; /* error1 easy */
		}

		protected void testReturnNull(String param1) {
			testReturnNull(null); /* error1 easy */
		}

	}

	void test() {
		new Object() {

			protected String testReturnNull() {
				return null; /* error1 easy */
			}

			protected void testReturnNull(String param1) {
				testReturnNull(null); /* error1 easy */
			}

		};

	}

	class ClassAnnotationTest2 extends ClassAnnotationTest {

		protected String testReturnNull() {
			return null; /* error1 easy */
		}

	}

}