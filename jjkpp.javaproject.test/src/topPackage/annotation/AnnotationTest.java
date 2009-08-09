package topPackage.annotation;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.CanBeNullParam2;
import jjkpp.jdt.annotations.NonNull;
import jjkpp.jdt.annotations.NonNullParam1;
import jjkpp.jdt.annotations.NonNullParam2;

public class AnnotationTest {
	
	@NonNullParam1 @CanBeNullParam2/*OK*/
	private void testAnnotatedParameters(String s1, String s2) {
	}
	
	@NonNullParam1 @CanBeNullParam1/*error0*/
	private void testOppositeAnnotationsOnParameter(String s1) {
	}
	
	@NonNull @CanBeNull/*error0*/
	private String testOppositeAnnotationsOnReturn(String s1) {
		return "";
	} 
	 
	@NonNull /*error0*//*TODO allow or not?*/
	private void testReturnAnnotationOnVoid(String s1) {
	} 
	
	@NonNullParam2 /*error0*/
	private void testOutOfBoundIndex2(String s1) {
	} 
	 
	@NonNullParam1 /*error0*/
	private void testOutOfBoundIndex1() {
	}

	@NonNull @CanBeNull/*error0*/
	private String testDoubleAnnotation(String s1) {
		 return "";
	}
	
}