package topPackage;

import java.util.Random;

import findnpe.annotations.CanBeNull;

public class NullStatusEnhancedTest {

	@CanBeNull
	String f;

	void testArrayAllocationExpressionSophisticated1() {
		String s = null;
		String ss = "";
		String[] x = new String[] { s = ss, s };
		x[10].toString(); /* OK */
	}

	void testArrayAllocationExpressionSophisticated2() {
		String s = null;
		String ss = "";
		String[] x = new String[] { s, s = ss };
		x[10].toString(); /* error1 */
	}

	void testArrayAllocationExpressionSophisticated3() {
		String s = "";
		String ss = null;
		String[] x = new String[] { "" + (s = ss), s };
		x[10].toString(); /* error1 */
	}

	void testConditionalExpression1UsingLocalDeclaration() {
		String s = new Random().nextBoolean() ? "" : null;
		String ss = (s != null) ? s : "";
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
