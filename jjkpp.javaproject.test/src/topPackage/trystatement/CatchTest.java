package topPackage.trystatement;

import java.io.File;
import java.io.IOException;

public class CatchTest {
	
	private void testNonNullAfter() {
		String s="";
		try {			
		}
		catch (Exception e) {
			
		} 
		s.toString(); /*OK*/
	}	
	

	private void testCanBeNullAfter1() {
		String s="";
		try {			
			"".toString();
		}
		catch (Exception e) {
			s=null;			
		} 
		s.toString(); /*error1*/
	}	
	
	private void testCanBeNullAfter2() {
		String s="";
		try {			
		}
		catch (Exception e) {
			s=null;			
		} 
		s.toString(); /*OK*/
	}	
	
	private void testCanBeNullAfter3() {
		String s="";
		try {	
			s=null;			
		}
		catch (Exception e) {
		} 
		s.toString(); /*error1*/
	}	 
		
	private void testUseNullInfo() {
		String s="";
		try {	
			"".toString();		
		}
		catch (Exception e) {
			s.toString(); /*OK*/
		} 
	}	

	private void testUseNullInfo2() {
		String s=null;
		try {		
			"".toString();
		}
		catch (Exception e) {
			s.toString(); /*error1*/
		} 
	}	 
	
	private void testCheckedUseNullInfo() {
		String s="";
		try {		
			new File("").createNewFile();
		}
		catch (IOException e) {
			s.toString(); /*OK*/
		} 
	}	


	private void testUsePotentialNullInfoFromTryBlock() {
		String s=null;
		try {		
			s="";
			new File("").createNewFile();
		}
		catch (IOException e) {
			s.toString(); /*error1*/
		} 
	}	

	private void testCanBeNull1() {
		String s=null;
		try {	
			if ("".contains("")) {
				throw new RuntimeException("");
			}
			s="";			
		}
		catch (Exception e) {	
			s.toString(); /*error1*/
		}  
	}	 

	private void testCanBeNull() {
		String s="";
		try {	
			s=null;			
		}
		catch (Exception e) {
			s.toString(); /*error1*/
		} 
	}	 

}