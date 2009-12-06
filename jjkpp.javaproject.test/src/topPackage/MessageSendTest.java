package topPackage;

import jjkpp.jdt.annotations.CanBeNull;
import jjkpp.jdt.annotations.NonNull;

public class MessageSendTest {

	public MessageSendTest(@NonNull String param1) {
	}

	public MessageSendTest(@CanBeNull String[] param1) {
	}

	public MessageSendTest(@NonNull Object param1) {
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

	private void testUnknownValueToNonNullParameter(@NonNull String param1) {
		String s = "";
		if (s.equals("x")) {
			s = null;
		}
		testUnknownValueToNonNullParameter(s/* error1 */);
	}

	private void testPassNullToNonNullParameter(@NonNull String param1) {
		testPassNullToNonNullParameter(null/* error0 easy */);
	}

	private void testPassNullToCanBeNullParameter(@CanBeNull String param1) {
		testPassNullToCanBeNullParameter(null/* OK */);
	}

	private void testPassNonNullValueToNonNullParameter(@NonNull String param1) {
		testPassNonNullValueToNonNullParameter(param1/* OK */);
	}

	private void testPassUnknownValueToNonNullParameter(@CanBeNull String param1) {
		testPassUnknownValueToNonNullParameter(param1/* OK */);
	}

}