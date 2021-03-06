package topPackage.field;

import findnpe.annotations.CanBeNull;
import findnpe.annotations.NonNull;

public class CheckFieldTest {

	@CanBeNull
	String s5 = null/* OK */;

	@NonNull
	String s6;
	
	String s7;

	private void testSingleNameReference() {
		if (s5 != null)
			s5.toString(); /* OK */
		s5.toString(); /* error0 */
		s5.toString(); /* OK */
	}

	private void testLocalVariable() {
		String s5 = "".equals("") ? "" : null;
		if (s5 != null)
			s5.toString(); /* OK */
		s5.toString(); /* error1 */
		s5.toString(); /* OK */
	}

	private void testNormal() {
		s5.toString(); /* error0 */
	}

	private void testFieldReference() {
		if (this.s5 != null)
			this.s5.toString(); /* OK */
		this.s5.toString(); /* error0 */
		this.s5.toString(); /* OK */
	}

	/**
	 * Tests that a QualifiedNameReference is not treated
	 */
	private void testQualifiedNameReference() {
		CheckFieldTest var = this;
		if (var.s5 != null)
			var.s5.toString(); /* error0 */
	}	

	private void testNonNullFields() {
		s6.toString(); /* OK */
		if (s6 == null)
			s6.toString(); /* OK */ // field-as-local-faking not done here
	}

	private void testNonNullFieldsAttack() {
		s7.toString(); /* OK */
		if (s7 == null)
			s7.toString(); /* OK */ // field-as-local-faking not done here
	}

	private class InnerClass {

		void work() {
			if (s5 != null)
				s5.toString(); /* error0 */ // field-as-local-faking not done here
			s5.toString(); /* error0 */
			s5.toString(); /* error0 */ // field-as-local-faking not done here
		}

	}
}