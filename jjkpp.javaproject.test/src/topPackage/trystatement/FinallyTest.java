package topPackage.trystatement;


public class FinallyTest {
	
	private void testNonNullAfter() {
		String s="";
		try {			
		}
		finally {
			
		} 
		s.toString(); /*OK*/
	}	
	
	private void testNonNullAfter2() {
		String s=null;
		try {
			s="";
		}
		finally {
			
		} 
		s.toString(); /*OK*/
	}	
	
	private void testNonNullAfter3() {
		String s=null;
		try {
		}
		finally {
			s="";			
		} 
		s.toString(); /*OK*/
	}	


	private void testCanBeNullAfter1() {
		String s="";
		try {			
		}
		finally {
			s=null;			
		} 
		s.toString(); /*error1*/
	}	
	
	private void testCanBeNullAfter2() {
		String s="";
		try {	
			s=null;			
		}
		finally {		
		} 
		s.toString(); /*error1*/
	}	 
	
	private void testUseNullInfo() {
		String s="";
		try {			
		}
		finally {
			s.toString(); /*OK*/
		} 
	}	
	
	private void testUseNullInfo1() {
		String s="";
		try {			
			s.toString(); /*OK*/  
		}
		finally {
		} 
	}		

	private void testCanBeNull1() {
		String s="";
		try {	
			s=null;			
		}
		finally {		
			s.toString(); /*TODO should be error, due to try block and finally block are analyzed independent from each other, but our hack uses the null info before try block */
		} 
	}	 

}