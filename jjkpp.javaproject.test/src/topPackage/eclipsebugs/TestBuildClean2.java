package topPackage.eclipsebugs;

import java.util.Collection;
import java.util.Iterator;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNullByDefault;

@NonNullByDefault
public class TestBuildClean2 extends TestBuildClean {

	public void testCallBuildClean2() {
		testBuildClean("");
	}
}
