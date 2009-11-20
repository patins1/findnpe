package topPackage.field;

public class CheckFieldSubclassTest extends CheckFieldTest {

	private void testSingleNameReference() {
		if (s5 != null)
			s5.toString(); /* error0 */
	}

	private void testFieldReference() {
		if (this.s5 != null)
			this.s5.toString(); /* error0 */
	}
}