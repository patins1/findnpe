package topPackage.iteration;

import pingpong.annotations.CanBeNull;

public class WhileTest {

	@CanBeNull
	private String fx;

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

	private void testDoubleCheckWithField() {
		fx = "";
		fx.toString();/* OK */
		while ("".contains("")) {
			fx.toString();/* error0 */
			fx = null;
		}
	}

	private void testDoubleCheckWithCondition() {
		String x = "";
		while (x != null) {
			x.toString();/* OK */
			x = null;
		}
	}

	private void testNoDoubledProblems() {
		String x = null;
		String y = "";
		while ("".contains("")) {
			while ("".contains("")) {
				x.toString();/* error1 */
				y = null;
			}
			x.toString();/* error1 */
			y = null; // forces a second pass
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

	private void testDoubleCheckUsingOnlyOneExpressionAsBodyI() {
		Integer i = 1;
		while ("".contains(""))
			i = "".contains("") ? (i + 1) : null;/* error1 */
	}

}