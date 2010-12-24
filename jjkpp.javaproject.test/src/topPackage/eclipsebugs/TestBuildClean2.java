package topPackage.eclipsebugs;

import findnpe.annotations.NonNullByDefault;

@NonNullByDefault
public class TestBuildClean2 extends TestBuildClean {

	public void testCallBuildClean2() {
		testBuildClean("");
	}
}
