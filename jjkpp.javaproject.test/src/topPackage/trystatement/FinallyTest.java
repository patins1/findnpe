package topPackage.trystatement;

public class FinallyTest {

	private void testFinallyUnreachable(Class parent) {
		try {
			return;
		} finally {
			"".equals("");
		}
	}

	private void testFinallyUnreachable_CanBeNull(Class parent) {
		String s = "";
		try {
			s = null;
			return;
		} finally {
			s.toString(); /* OK TODO */
		}
	}

	private void testNonNullAfter() {
		String s = "";
		try {
		} finally {

		}
		s.toString(); /* OK */
	}

	private void testNonNullAfter2() {
		String s = null;
		try {
			s = "";
		} finally {

		}
		s.toString(); /* OK */
	}

	private void testNonNullAfter3() {
		String s = null;
		try {
		} finally {
			s = "";
		}
		s.toString(); /* OK */
	}

	private void testCanBeNullAfter1() {
		String s = "";
		try {
		} finally {
			s = null;
		}
		s.toString(); /* error1 */
	}

	private void testCanBeNullAfter2() {
		String s = "";
		try {
			s = null;
		} finally {
		}
		s.toString(); /* error1 */
	}

	private void testUseNullInfo() {
		String s = "";
		try {
		} finally {
			s.toString(); /* OK */
		}
	}

	private void testUseNullInfo1() {
		String s = "";
		try {
			s.toString(); /* OK */
		} finally {
		}
	}

	private void testCanBeNull1() {
		String s = "";
		try {
			s = null;
		} finally {
			s.toString(); /* error1 */
		}
	}

	private void testNoDoubledProblems() {
		String s = null;
		try {
			"".equals("");
		} finally {
			s.toString(); /* error1 */
		}
	}

}