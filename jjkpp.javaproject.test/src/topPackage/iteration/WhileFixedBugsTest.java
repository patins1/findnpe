package topPackage.iteration;

import java.nio.charset.CoderResult;

public class WhileFixedBugsTest {

	public static void testNotUnreachableCodeAfterSecondAnalysis() {
		String s = "";
		while (true) {
			s = "1";
			break;
		}
	}

	public static void testNoCompileError() {

		CoderResult result = null;
		while (true) {
			if ("".equals("")) {
				String s = "";
				s.toString();
			} else {
				break;
			}
		}

	}

}