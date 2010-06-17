package topPackage.field;

import pingpong.annotations.CanBeNull;

public class CheckFieldTest {

	@CanBeNull
	String s5 = null/* OK */;

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

	private class InnerClass {

		void work() {
			if (s5 != null)
				s5.toString(); /* OK */
			s5.toString(); /* error0 */
			s5.toString(); /* OK */
		}

	}
}