package topPackage.iteration;

public class WhileTest {

	private void testUseNullInfo() {
		String x = "";
		while ("".contains("")) {
			x.toString();/* ok */
		}
		x.toString();
	}

	private void testDoubleCheck() {
		String x = "";
		while ("".contains("")) {
			x.toString();/* error1 */
			x = null;
		}
	}

	private void testEnduringNullInfoChange() {
		String x = "";
		while ("".contains("")) {
			x = null;
		}
		x.toString();/* error1 */
	}

	private void testDoubleCheckUsingOnlyOneExpressionAsBody() {
		String x = "";
		while ("".contains(""))
			x = "".contains("") ? x.toString() : null;/* error1 */
	}

}