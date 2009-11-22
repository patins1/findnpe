package topPackage;

import jjkpp.jdt.annotations.CanBeNullParam1;
import jjkpp.jdt.annotations.NonNullParam1;

public class MessageSendTest {

	@NonNullParam1
	public MessageSendTest(String param1) {
	}

	@CanBeNullParam1
	public MessageSendTest(String[] param1) {
	}

	@NonNullParam1
	public MessageSendTest(Object param1) {
	}

	public MessageSendTest(Number param1) {
	}

	public MessageSendTest(Boolean param1) {
	}

	private void testConstructor() {
		new MessageSendTest((String) null/* error0 easy */);
		new MessageSendTest((String) null/* error0 easy */) {
		};

		new MessageSendTest((String[]) null/* OK */);
		new MessageSendTest((String[]) null/* OK */) {
		};

		new MessageSendTest((Boolean) null/* error1 easy NOATTACK */);
		new MessageSendTest((Boolean) null/* error1 easy NOATTACK */) {
		};

		new MessageSendTest((Object) null/* error0 easy */);
		new MessageSendTest((Object) null/* error0 easy */) {
		};

		new MessageSendTest((Integer) null/* error1 easy NOATTACK */);
		new MessageSendTest((Integer) null/* error1 easy NOATTACK */) {
		};
	}

	@NonNullParam1
	private void testUnknownValueToNonNullParameter(String param1) {
		String s = "";
		if (s.equals("x")) {
			s = null;
		}
		testUnknownValueToNonNullParameter(s/* error1 */);
	}

	@NonNullParam1
	private void testPassNullToNonNullParameter(String param1) {
		testPassNullToNonNullParameter(null/* error0 easy */);
	}

	@CanBeNullParam1
	private void testPassNullToCanBeNullParameter(String param1) {
		testPassNullToCanBeNullParameter(null/* OK */);
	}

	@NonNullParam1
	private void testPassNonNullValueToNonNullParameter(String param1) {
		testPassNonNullValueToNonNullParameter(param1/* OK */);
	}

	@CanBeNullParam1
	private void testPassUnknownValueToNonNullParameter(String param1) {
		testPassUnknownValueToNonNullParameter(param1/* OK */);
	}

}