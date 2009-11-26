package jjkpp.jdt.test;

import jjkpp.jdt.core.classes.NullibilityAnnos;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;

public class TestNullBits extends TestCase {

	public void testPreMerge() {
		for (NullBitsCombi combi : NullBitsCombi.values()) {
			
			UnconditionalFlowInfo a;
			UnconditionalFlowInfo b;
			
			a = combi.bits();
			b = NullBitsCombi.ProtNull.bits();
			a.mergedWith(b);
			Object c1 = NullBitsCombi.find(a);

			a = combi.bits();
			b = NullBitsCombi.ProtNull.bits();
			a.mergedWith(NullibilityAnnos.mergedWithPrepare(a, b));
			Assert.assertSame(NullBitsCombi.ProtNull, NullBitsCombi.find(b));
			Object c2 = NullBitsCombi.find(a);

			a = NullBitsCombi.ProtNull.bits();
			b = combi.bits();
			a.mergedWith(NullibilityAnnos.mergedWithPrepare(a, b));
			Assert.assertSame(combi, NullBitsCombi.find(b));
			Object c3 = NullBitsCombi.find(a);

			if (combi.equals(NullBitsCombi.DefNonnull)) {
				Assert.assertNotSame(NullBitsCombi.DefNonnull, c1);
				Assert.assertEquals(NullBitsCombi.DefNonnull, c2); // testMergeDefNNAndProtNull
				Assert.assertEquals(NullBitsCombi.DefNonnull, c3); // testMergeProtNullAndDefNN
			} else {
				Assert.assertSame(c1, c2);
				Assert.assertSame(c1, c3);
			}

			System.out.println(combi + (combi.equals(c2) ? "" : " => " + c2));
		}

	}

}
