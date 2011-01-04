package topPackage.annotation;

import findnpe.annotations.CanBeNull;
import findnpe.annotations.NonNull;

public class ClassAnnotationTest31 extends ClassAnnotationTest3 {

	public ClassAnnotationTest31(String s) {
		super(s);
	}

	@Override
	protected String testAbstractOverrideProposal() {
		super.testOverrideProposal().toString();  /* error1 */
		return null;
	}

}
