package topPackage.originalbinding;

import findnpe.annotations.CanBeNull;
import findnpe.annotations.NonNullByDefault;

@NonNullByDefault
public class ParametrizedUtil {

	public static <T extends Object> void getSnippetLookupMap(@CanBeNull T object) {
	}

	public static <T extends Object> void getSnippetLookupMap2(T object) {
	}
}
