package topPackage.originalbinding;

import pingpong.annotations.CanBeNull;
import pingpong.annotations.NonNullByDefault;

@NonNullByDefault
public class ParametrizedUtil {

	public static <T extends Object> void getSnippetLookupMap(@CanBeNull T object) {
	}

	public static <T extends Object> void getSnippetLookupMap2(T object) {
	}
}
