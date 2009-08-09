package topPackage.annotation;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNull;
import jjkpp.jdt.annotations.NonNullParam1;

public class OverrideAnnotationTest {

	@NonNullParam1/*OK*/
	protected void testParamNonNull(String s) {
	}
	
	@CanBeNullParam1/*OK*/
	protected String testParamCanBeNull(String s) {
		return "";
	} 

	@NonNull/*OK*/
	protected String testReturnNonNull() {
		return "";
	}
	
	@CanBeNull/*OK*/
	protected String testReturnCanBeNull() {
		return "";
	}
	
	@CanBeNullParam1
	protected String testReturnAndParamError(String s) {
		return "" ;      				
	}  
	
	void test() {
		new OverrideAnnotationTest() {

			@CanBeNull/*error0*/
			protected String testReturnNonNull() {
				return "" ;      				
			}  
			
			@NonNull/*OK*/
			protected String testReturnCanBeNull() {
				return "";
			}   
			
			@CanBeNullParam1/*OK*/
			protected void testParamNonNull(String s) {
			}    
			     
			@NonNull @NonNullParam1/*error0*/
			protected String testParamCanBeNull(String s) {
				return "";
			} 

			@NonNullParam1/*error0*/
			@CanBeNull/*error0*/ 
			protected String testReturnAndParamError(String s) {
				return "" ;      				
			}  
		};
		
	}
	
}