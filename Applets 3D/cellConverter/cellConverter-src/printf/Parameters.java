// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov Date: 19.08.2005
// 10:54:15
// Home Page : http://members.fortunecity.com/neshkov/dj.html - Check often for
// new version!
// Decompiler options: fullnames safe

package printf;

import java.util.Vector;

// Referenced classes of package com.braju.format:
// ParametersAutoClear

public class Parameters {

	public Parameters() {
		this.a = new Vector();
		this.a.addElement(((new ParametersAutoClear())));
	}

	public Parameters(boolean flag) {
		this();
		this.add(flag);
	}

	public Parameters(char c) {
		this();
		this.add(c);
	}

	public Parameters(byte byte0) {
		this();
		this.add(byte0);
	}

	public Parameters(short word0) {
		this();
		this.add(word0);
	}

	public Parameters(int i) {
		this();
		this.add(i);
	}

	public Parameters(long l) {
		this();
		this.add(l);
	}

	public Parameters(float f) {
		this();
		this.add(f);
	}

	public Parameters(double d) {
		this();
		this.add(d);
	}

	public Parameters(java.lang.String s) {
		this();
		this.add(s);
	}

	public Parameters(java.lang.Object obj) {
		this();
		this.add(obj);
	}

	public Parameters(java.util.Vector vector) {
		this.a = vector;
	}

	public java.util.Vector toVector() {
		return this.a;
	}

	public Parameters add(boolean flag) {
		this.a.addElement(((new Boolean(flag))));
		return this;
	}

	public Parameters add(char c) {
		this.a.addElement(((new Character(c))));
		return this;
	}

	public Parameters add(byte byte0) {
		this.a.addElement(((new Byte(byte0))));
		return this;
	}

	public Parameters add(short word0) {
		this.a.addElement(((new Short(word0))));
		return this;
	}

	public Parameters add(int i) {
		this.a.addElement(((new Integer(i))));
		return this;
	}

	public Parameters add(long l) {
		this.a.addElement(((new Long(l))));
		return this;
	}

	public Parameters add(float f) {
		this.a.addElement(((new Float(f))));
		return this;
	}

	public Parameters add(double d) {
		this.a.addElement(((new Double(d))));
		return this;
	}

	public Parameters add(java.lang.String s) {
		this.a.addElement(((s)));
		return this;
	}

	public Parameters add(java.lang.Object obj) {
		this.a.addElement(obj);
		return this;
	}

	public Parameters autoClear(boolean flag) {
		if (this.isAutoClear()) {
			if (!flag)
				this.a.removeElementAt(0);
		} else if (flag)
			this.a.insertElementAt(((new ParametersAutoClear())), 0);
		return this;
	}

	public boolean isAutoClear() {
		return this.a.firstElement() instanceof ParametersAutoClear;
	}

	public Parameters clear() {
		if (this.isAutoClear()) {
			this.a.removeAllElements();
			this.autoClear(true);
		} else
			this.a.removeAllElements();
		return this;
	}

	private java.util.Vector a;
}

class ParametersAutoClear {

	public ParametersAutoClear() {
	}
}