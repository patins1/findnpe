package topPackage;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;

public class ReturnTest {
	
	
	@NonNull
	private String testNonNullReturn() {
		testNonNullReturn().toString();/*OK*/ 
		return null/*error0 easy*/;
	}
	 
	@CanBeNull  
	private String testNullReturn() {
		testNullReturn().toString();/*error0*/  
		return null/*OK*/;   
	}    
	  
}