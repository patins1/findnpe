package topPackage;

import java.util.Random;

import findnpe.annotations.CanBeNull;

public class NullStatusEnhancedTest {

	@CanBeNull
	String f;

	void testArrayAllocationExpressionSophisticated1() {
		String s = null;
		String ss = "";
		String r;
		String[] x = new String[] { s = ss, r = s };
		r.toString(); /* OK */
	}

	void testArrayAllocationExpressionSophisticated2() {
		String s = null;
		String ss = "";
		String r;
		String[] x = new String[] { r = s, s = ss };
		r.toString(); /* error1 */
	}

	void testArrayAllocationExpressionSophisticated3() {
		String s = "";
		String ss = null;
		String r;
		String[] x = new String[] { "" + (s = ss), r = s };
		r.toString(); /* error1 */
	}

	void testConditionalExpression1UsingLocalDeclaration() {
		String s = new Random().nextBoolean() ? "" : null;
		String ss = (s != null) ? s : "";
		ss.toCharArray(); /* OK */
	}

	void testConditionalExpressionWithDeadCodeExpression() {
		String t = "";
		String s = "";
		String ss = (s != null) ? s : t/*dead code*/;
		ss.toCharArray(); /* OK */
	}

	void testConditionalExpression1UsingAssignment() {
		String s = new Random().nextBoolean() ? "" : null;
		String ss = (s != null) ? s : "";
		ss.toCharArray(); /* OK */
	}

	void testConditionalExpression2() {
		String s = new Random().nextBoolean() ? "" : null;
		String ss = (s != null) ? "" : s;
		ss.toCharArray(); /* error1 */
	}
}
