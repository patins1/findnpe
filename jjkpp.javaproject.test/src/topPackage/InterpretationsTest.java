package topPackage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import junit.framework.Assert;

import org.eclipse.swt.SWT;

public class InterpretationsTest {

	void testAssertNull() {
		String s = new Random().nextBoolean() ? "" : null;
		Assert.assertNull("No HazardousEvent found in the model", s);
		s.toString(); /* error1 */
	}

	void testAssertNotNull() {
		String s = new Random().nextBoolean() ? "" : null;
		Assert.assertNotNull("No HazardousEvent found in the model", s);
		s.toString(); /* ok */
	}

	void testFail() {
		String s = new Random().nextBoolean() ? "" : null;
		if (s == null)
			Assert.fail();
		s.toString(); /* ok */
	}

	void testIsNotNull() {
		String s = new Random().nextBoolean() ? "" : null;
		org.eclipse.core.runtime.Assert.isNotNull(s);
		s.toString(); /* ok */
	}

	void testSWTError() {
		String s = new Random().nextBoolean() ? "" : null;
		if (s == null)
			SWT.error(0);
		s.toString(); /* ok */
	}

	void testFailInCatch() {
		String result = null;
		try {
			Thread.sleep(3);
			new File("").createNewFile();
			result = "";
		} catch (InterruptedException e) {
			org.junit.Assert.fail();
		} catch (IOException e) {
			junit.framework.Assert.fail(e.getLocalizedMessage());
		}
		result.toString(); /* ok */
	}
}