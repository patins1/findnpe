package topPackage.annotation;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNullByDefault;

@NonNullByDefault
public class ClassAnnotationTest {

	public ClassAnnotationTest(@CanBeNull String s) {

	}

	public ClassAnnotationTest(Integer s) {

	}

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

		public ClassAnnotationTest2(String s) {
			super(s);
		}

		protected String testReturnNull() {
			return null; /* error1 easy */
		}

	}

}