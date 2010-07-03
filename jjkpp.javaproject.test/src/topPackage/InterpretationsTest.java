package topPackage;

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

}