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

	private void testEnduringNullInfoChange() {
		String x = "";
		for (int i = 0; i < 10; i++) {
			x = null;
		}
		x.toString();/* error1 */
	}

}