package topPackage.iteration;

public class DoWhileTest {

	private void testUseNullInfo() {
		String x = "";
		do {
			x.toString();/* ok */
		} while ("".contains(""));
		x.toString();
	}

	private void testDoubleCheck() {
		String x = "";
		do {
			x.toString();/* error1 */
			x = null;
		} while ("".contains(""));
	}

	private void testEnduringNullInfoChange() {
		String x = "";
		do {
			x = null;
		} while ("".contains(""));
		x.toString();/* error1 */
	}

}