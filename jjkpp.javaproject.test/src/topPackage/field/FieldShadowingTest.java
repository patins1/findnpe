/* 
 * Copyright (c) 2010 ikv++ technologies ag
 * All rights reserved.
 */
package topPackage.field;

import findnpe.annotations.CanBeNull;

public class FieldShadowingTest {

	@CanBeNull 
	private String s1;

	private class InnerClass {

		@CanBeNull 
		private String s2; // s2 takes the same id as s1, so test that s1 is not

		// faked as local in inner class to prevent conflict

		public boolean testSingleNameReferenceShadowing() {
			if (this.s2 == null) {
				return true;
			}
			s1.toString(); /* error0 */
			return false;
		}

		public boolean testFieldReferenceShadowing() {
			if (this.s2 == null) {
				return true;
			}
			FieldShadowingTest.this.s1.toString(); /* error0 */
			return false;
		}

	}

	public boolean testSingleNameReferenceNormal() {
		if (this.s1 == null) {
			return true;
		}
		s1.toString(); /* ok */
		return false;
	}

	public boolean testFieldReferenceNormal() {
		if (this.s1 == null) {
			return true;
		}
		FieldShadowingTest.this.s1.toString(); /* ok */
		return false;
	}

}
