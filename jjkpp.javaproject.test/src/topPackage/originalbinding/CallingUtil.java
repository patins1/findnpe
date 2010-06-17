/* 
 * Copyright (c) 2009 ikv++ technologies ag
 * All rights reserved.
 */
package topPackage.originalbinding;


public class CallingUtil {

	public void testAnnotatedParametrizedParameter() {
		ParametrizedUtil.getSnippetLookupMap(null); /* OK */
	}

	public void testNotAnnotatedParametrizedParameter() {
		ParametrizedUtil.getSnippetLookupMap2(null); /* error1 easy */
	}

}
