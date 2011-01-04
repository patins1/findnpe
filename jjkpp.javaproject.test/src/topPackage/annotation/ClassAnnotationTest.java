package topPackage.annotation;

import findnpe.annotations.CanBeNull;
import findnpe.annotations.NonNull;
import findnpe.annotations.NonNullByDefault;

@NonNullByDefault
abstract public class ClassAnnotationTest {

	public ClassAnnotationTest(@CanBeNull String s) {

	}

	public ClassAnnotationTest(Integer s) {

	}

	public ClassAnnotationTest(int[] param1) {
		this((Integer) null); /* error1 easy */
	}

	protected String testReturnNull() {
		return null; /* error1 easy */
	}

	protected void testReturnNull(String param1) {
		testReturnNull(null); /* error1 easy */
	}

	@CanBeNull
	protected String testOverrideProposal() {
		return null; 
	}

	@CanBeNull
	abstract protected String testAbstractOverrideProposal();

	@NonNull
	protected String testNoOverrideProposal() {
		return ""; 
	}

	protected void testParameterOverrideProposal(@NonNull String s) {
	}

	protected void testNoParameterOverrideProposal(@CanBeNull String s) {
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

		@Override
		protected String testAbstractOverrideProposal() {
			return null;
		}

	}

}