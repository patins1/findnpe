package topPackage;

import java.util.Random;

import jjkpp.jdt.annotations.CanBeNull;

public class NullStatusTest {
	
	@CanBeNull
	String f;	

	void testQualifiedNameReference() {
		NullStatusTest t=this;
		t.f.toString();/*error0*/
	}   

	void testSingleNameReference() {
		NullStatusTest t=null;
		t.toString();/*error1*/
	}    

	void testFieldReference() {
		f.toString();/*error0*/
	}
	
	@CanBeNull
	String testMessageSend() {
		testMessageSend().toString();/*error0*/
		return null;
	}
	
	void testArrayAllocationExpression1() {		
		String[] x=new String[] {"x",null}; 
		x[10].toString(); /*error1*/	    
		x.toString(); /*ok*/  	
	}
	void testArrayAllocationExpression2() {		
		String[] x=new String[] {"x",null};     
		x.toString(); /*error1*/  	
		x[10].toString(); /*ok*/	
	}
	void testArrayAllocationExpression3() {		
		String[] x=new String[] {"x",null}; 
		String[] y=new String[] {};
		y[0].toString(); /*ok*/
		y=x;
		y[0].toString(); /*error1*/  
	}

	void testArrayAllocationExpressionSophisticated1() {		
		String s=null;
		String ss="";
		String[] x=new String[] {s=ss,s}; 
		x[10].toString(); /*error1*/ //TODO should be ok	 
	}
	
	void testArrayAllocationExpressionSophisticated2() {		
		String s=null;
		String ss="";
		String[] x=new String[] {s,s=ss}; 
		x[10].toString(); /*error1*/	 
	}	

	void testConditionalExpression1() {
		String s=new Random().nextBoolean()?"":null;
		String ss = (s!=null) ? s : "";
		ss.toCharArray(); /*error1*/ //TODO should be ok
	}
	
	void testConditionalExpression2() {
		String s=new Random().nextBoolean()?"":null;
		String ss = s;
		ss.toCharArray(); /*error1*/
	}
	
	void testConditionalExpression3() {
		String s=new Random().nextBoolean()?"":null;
		String ss = (s!=null) ? "" : "";
		ss.toCharArray(); /*ok*/
	}

	void testAssignmentExpression() {
		String s="".toString(); 
		String ss = (s=null);
		ss.toCharArray(); /*error1*/
	}

	
}
