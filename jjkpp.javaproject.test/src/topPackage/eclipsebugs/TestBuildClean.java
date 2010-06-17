package topPackage.eclipsebugs;

import pingpong.annotations.CanBeNull;
import pingpong.annotations.NonNull;
import pingpong.annotations.NonNullByDefault;

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
