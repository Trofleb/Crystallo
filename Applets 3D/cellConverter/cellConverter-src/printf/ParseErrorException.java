// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 19.08.2005
// 10:55:14
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: fullnames safe

package printf;

public class ParseErrorException extends java.lang.RuntimeException {

	public ParseErrorException() {
		this.a = new String("Unexpected character.");
	}

	public ParseErrorException(java.lang.String s) {
		this.a = new String(s);
	}

	public java.lang.String toString() {
		return this.a;
	}

	java.lang.String a;
}