package topPackage.eclipsebugs;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;
import jjkpp.jdt.annotations.NonNullByDefault;

@NonNullByDefault
public class TestBuildClean {

	@NonNull
	public void testBuildClean(@CanBeNull String param1) {

	}

	public void testCallBuildClean() {
		// without our patch, an error would be generated at a clean-build
		testBuildClean(null); /* ok */
	}
}
