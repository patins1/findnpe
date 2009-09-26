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

	private void testDoubleCheckWithCondition() {
		String x = "";
		do {
			x.toString();/* OK */
			x = null;
		} while (x != null);
	}

	private void testNoDoubledProblems() {
		String x = null;
		String y = "";
		do {
			do {
				x.toString();/* error1 */
			} while ("".contains(""));
			x.toString();/* OK */
			y.toString();/* error1 */
			y = null; // forces a second pass
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