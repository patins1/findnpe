package topPackage;

import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNullParam1;

public class MessageSendTest {
	

	@NonNullParam1
	public MessageSendTest(String param1) {		 
		new MessageSendTest((String)null/*error0 easy*/); 
		new MessageSendTest((String)null/*error0 easy*/) {}; 
	}

	@CanBeNullParam1
	public MessageSendTest(String[] param1) {		
		new MessageSendTest((String[])null/*OK*/);
	}
	
	public MessageSendTest(Boolean param1) {		
		new MessageSendTest((Boolean)null/*error1 easy*/);
	}
	
	@NonNullParam1
	private void testUnknownValueToNonNullParameter(String param1) {
		String s="";
		if (s.equals("x")) {
			s=null;
		} 
		testUnknownValueToNonNullParameter(s/*error1*/); 
	}
	
	@NonNullParam1
	private void testPassNullToNonNullParameter(String param1) {
		testPassNullToNonNullParameter(null/*error0 easy*/); 
	}
	
	@CanBeNullParam1
	private void testPassNullToCanBeNullParameter(String param1) {
		testPassNullToCanBeNullParameter(null/*OK*/);  
	}
	
	@NonNullParam1
	private void testPassNonNullValueToNonNullParameter(String param1) {
		testPassNonNullValueToNonNullParameter(param1/*OK*/); 
	}
	
	@CanBeNullParam1
	private void testPassUnknownValueToNonNullParameter(String param1) {
		testPassUnknownValueToNonNullParameter(param1/*OK*/); 
	}

}