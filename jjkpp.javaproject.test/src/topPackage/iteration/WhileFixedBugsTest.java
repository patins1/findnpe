package topPackage.iteration;

import java.nio.charset.CoderResult;

public class WhileFixedBugsTest {

	public void testNotUnreachableCodeAfterSecondAnalysis() {
		String s = "";
		while (true) {
			s = "1";
			break;
		}
	}

	public void testNoCompileError() {
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