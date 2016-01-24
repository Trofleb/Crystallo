// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 19.08.2005 10:54:15
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: fullnames safe 

package printf;

import java.util.Vector;

// Referenced classes of package com.braju.format:
//            ParametersAutoClear

public class Parameters
{

    public Parameters()
    {
        _fldif = false;
        a = new Vector();
        a.addElement(((java.lang.Object) (new ParametersAutoClear())));
    }

    public Parameters(boolean flag)
    {
        this();
        add(flag);
    }

    public Parameters(char c)
    {
        this();
        add(c);
    }

    public Parameters(byte byte0)
    {
        this();
        add(byte0);
    }

    public Parameters(short word0)
    {
        this();
        add(word0);
    }

    public Parameters(int i)
    {
        this();
        add(i);
    }

    public Parameters(long l)
    {
        this();
        add(l);
    }

    public Parameters(float f)
    {
        this();
        add(f);
    }

    public Parameters(double d)
    {
        this();
        add(d);
    }

    public Parameters(java.lang.String s)
    {
        this();
        add(s);
    }

    public Parameters(java.lang.Object obj)
    {
        this();
        add(obj);
    }

    public Parameters(java.util.Vector vector)
    {
        _fldif = false;
        a = vector;
    }

    public java.util.Vector toVector()
    {
        return a;
    }

    public Parameters add(boolean flag)
    {
        a.addElement(((java.lang.Object) (new Boolean(flag))));
        return this;
    }

    public Parameters add(char c)
    {
        a.addElement(((java.lang.Object) (new Character(c))));
        return this;
    }

    public Parameters add(byte byte0)
    {
        a.addElement(((java.lang.Object) (new Byte(byte0))));
        return this;
    }

    public Parameters add(short word0)
    {
        a.addElement(((java.lang.Object) (new Short(word0))));
        return this;
    }

    public Parameters add(int i)
    {
        a.addElement(((java.lang.Object) (new Integer(i))));
        return this;
    }

    public Parameters add(long l)
    {
        a.addElement(((java.lang.Object) (new Long(l))));
        return this;
    }

    public Parameters add(float f)
    {
        a.addElement(((java.lang.Object) (new Float(f))));
        return this;
    }

    public Parameters add(double d)
    {
        a.addElement(((java.lang.Object) (new Double(d))));
        return this;
    }

    public Parameters add(java.lang.String s)
    {
        a.addElement(((java.lang.Object) (s)));
        return this;
    }

    public Parameters add(java.lang.Object obj)
    {
        a.addElement(obj);
        return this;
    }

    public Parameters autoClear(boolean flag)
    {
        if(isAutoClear())
        {
            if(!flag)
                a.removeElementAt(0);
        } else
        if(flag)
            a.insertElementAt(((java.lang.Object) (new ParametersAutoClear())), 0);
        return this;
    }

    public boolean isAutoClear()
    {
        return a.firstElement() instanceof ParametersAutoClear;
    }

    public Parameters clear()
    {
        if(isAutoClear())
        {
            a.removeAllElements();
            autoClear(true);
        } else
        {
            a.removeAllElements();
        }
        return this;
    }

    private java.util.Vector a;
    private boolean _fldif;
}

class ParametersAutoClear
{

    public ParametersAutoClear()
    {
    }
}