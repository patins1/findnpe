package topPackage.field;

import jjkpp.jdt.annotations.CanBeNull;

public class CheckFieldLimitTest {

	@CanBeNull
	String s0;
	@CanBeNull
	String s1;
	@CanBeNull
	String s2;
	@CanBeNull
	String s3;
	@CanBeNull
	String s4;
	@CanBeNull
	String s5;
	@CanBeNull
	String s6;
	@CanBeNull
	String s7;
	@CanBeNull
	String s8;
	@CanBeNull
	String s9;
	@CanBeNull
	String s10;
	@CanBeNull
	String s11;
	@CanBeNull
	String s12;
	@CanBeNull
	String s13;
	@CanBeNull
	String s14;
	@CanBeNull
	String s15;
	@CanBeNull
	String s16;
	@CanBeNull
	String s17;
	@CanBeNull
	String s18;
	@CanBeNull
	String s19;
	@CanBeNull
	String s20;
	@CanBeNull
	String s21;
	@CanBeNull
	String s22;
	@CanBeNull
	String s23;
	@CanBeNull
	String s24;
	@CanBeNull
	String s25;
	@CanBeNull
	String s26;
	@CanBeNull
	String s27;
	@CanBeNull
	String s28;
	@CanBeNull
	String s29;
	@CanBeNull
	String s30;
	@CanBeNull
	String s31;
	@CanBeNull
	String s32;
	@CanBeNull
	String s33;
	@CanBeNull
	String s34;
	@CanBeNull
	String s35;
	@CanBeNull
	String s36;
	@CanBeNull
	String s37;
	@CanBeNull
	String s38;
	@CanBeNull
	String s39;
	@CanBeNull
	String s40;
	@CanBeNull
	String s41;
	@CanBeNull
	String s42;
	@CanBeNull
	String s43;
	@CanBeNull
	String s44;
	@CanBeNull
	String s45;
	@CanBeNull
	String s46;
	@CanBeNull
	String s47;
	@CanBeNull
	String s48;
	@CanBeNull
	String s49;
	@CanBeNull
	String s50;
	@CanBeNull
	String s51;
	@CanBeNull
	String s52;
	@CanBeNull
	String s53;
	@CanBeNull
	String s54;
	@CanBeNull
	String s55;
	@CanBeNull
	String s56;
	@CanBeNull
	String s57;
	@CanBeNull
	String s58;
	@CanBeNull
	String s59;
	@CanBeNull
	String s60;
	@CanBeNull
	String s61;
	@CanBeNull
	String s62;
	@CanBeNull
	String s63;
	@CanBeNull
	String s64;
	@CanBeNull
	String s65;
	@CanBeNull
	String s66;
	@CanBeNull
	String s67;
	@CanBeNull
	String s68;
	@CanBeNull
	String s69;

	private void testS0() {
		if (s0 != null)
			s0.toString(); /* OK */
		s0.toString(); /* error0 */
		s0.toString(); /* OK */
	}

	private void testS63() {
		if (s63 != null)
			s63.toString(); /* OK */
		s63.toString(); /* error0 */
		s63.toString(); /* OK */
	}

	private void testS64() {
		if (s64 != null)
			s64.toString(); /* error0 */
		s64.toString(); /* error0 */
		s64.toString(); /* error0 */
	}

	private void testS65() {
		if (s65 != null)
			s65.toString(); /* error0 */
		s65.toString(); /* error0 */
		s65.toString(); /* error0 */
	}

	private void testDoubleCheckWithFieldS63() {
		s63 = "";
		s63.toString();/* OK */
		while ("".contains("")) {
			s63.toString();/* error0 */
			s63 = null;
		}
	}

	private void testDoubleCheckWithFieldS64() {
		s64 = "";
		s64.toString();/* error0 */
		while ("".contains("")) {
			s64.toString();/* error0 */
			s64 = null;
		}
	}

}