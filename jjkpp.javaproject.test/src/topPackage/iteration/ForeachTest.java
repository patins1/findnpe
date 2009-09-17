package topPackage.iteration;

public class ForeachTest {

	private void testUseNullInfo() {
		String x = "";
		for (String s : new String[] { "" }) {
			x.toString();/* ok */
		}
		x.toString();
	}

	private void testDoubleCheck() {
		String x = "";
		for (String s : new String[] { "" }) {
			x.toString();/* error1 */
			x = null;
		}
	}

	private void testNoDoubledProblems() {
		String x = null;
		String y = "";
		for (String i : new String[] { "" }) {
			for (String j : new String[] { "" }) {
				x.toString();/* error1 */
			}
			x.toString();/* error1 */
			y = null; // forces a second pass
		}
	}

	private void testEnduringNullInfoChange() {
		String x = "";
		for (String s : new String[] { "" }) {
			x = null;
		}
		x.toString();/* error1 */
	}

	private void testElementVariableIsNotNull() {
		String x = "";
		for (String s : new String[] { "" }) {
			s.toString();/* ok */
		}
	}

	private void testRequireNonNullCollection() {
		String[] x = null;
		for (String s : x) {/* error1 */
			s.toString();
		}
	}

}