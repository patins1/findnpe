package topPackage.annotation;

import findnpe.annotations.CanBeNull;
import findnpe.annotations.NonNull;

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

	@Override
	protected String testOverrideProposal() {
		testOverrideProposal().toString();  /* error1 */
		return null;
	}

	@Override
	protected String testAbstractOverrideProposal() {
		testOverrideProposal().toString();  /* error1 */
		return null;
	}

	@Override
	protected String testNoOverrideProposal() {
		return null; /* error0 easy */
	}

	@Override
	protected void testParameterOverrideProposal(String s) {
		testParameterOverrideProposal(null);  /* error1 easy */
	}

	protected void testNoParameterOverrideProposal(String s) {
		s.toString();  /* error0 */	
	}

}
