package topPackage.iteration;

public class ForTest {

	private void testUseNullInfo() {
		String x = "";
		for (int i = 0; i < 10; i++) {
			x.toString();/* ok */
		}
		x.toString();
	}

	private void testDoubleCheck() {
		String x = "";
		for (int i = 0; i < 10; i++) {
			x.toString();/* error1 */
			x = null;
		}
	}

	private void testDoubleCheckWithCondition() {
		String x = "";
		for (int i = 0; i < 10 && x != null; i++) {
			x.toString();/* OK */
			x = null;
		}
	}

	private void testNoDoubledProblems() {
		String x = null;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				x.toString();/* error1 */
			}
			x.toString();/* error1 */
		}
	}

	private void testEnduringNullInfoChange() {
		String x = "";
		for (int i = 0; i < 10; i++) {
			x = null;
		}
		x.toString();/* error1 */
	}

}