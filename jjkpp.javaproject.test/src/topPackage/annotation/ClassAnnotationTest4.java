package topPackage.annotation;

import findnpe.annotations.NonNull;

public class ClassAnnotationTest4 extends ClassAnnotationTest {

	public ClassAnnotationTest4(String s) {
		super("");
		s.toString(); /* error0 */
	}

	@NonNull
	protected String testReturnNull() {
		return null; /* error0 easy */
	}

	@Override
	protected String testAbstractOverrideProposal() {
		return null;
	}

	@Override
	protected void testAbstractParameterOverrideProposal2(String s) {
	}

}
