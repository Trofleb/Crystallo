// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 19.08.2005 10:54:06
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: fullnames safe 

package printf;

import java.io.StringReader;
import java.util.Vector;

// Referenced classes of package com.braju.format:
//            a, ParametersAutoClear, b, c, 
//            Parameters, ParseErrorException

public class Format extends a
{

    public static void version()
    {
        java.lang.System.out.println("Java printf package version " + Format.getVersion());
    }

    private Format()
    {
    }

    private static void a(printf.c c1, java.lang.Object obj)
    {
        boolean flag = false;
        if(c1._fldbyte == 11)
            flag = true;
        else
        if(c1._fldbyte == 2 || c1._fldbyte == 3 || c1._fldbyte == 4 || c1._fldbyte == 1 || c1._fldbyte == 8 || c1._fldbyte == 6 || c1._fldbyte == 9)
            flag = (obj instanceof java.lang.Boolean) || (obj instanceof java.lang.Character) || (obj instanceof java.lang.Byte) || (obj instanceof java.lang.Short) || (obj instanceof java.lang.Integer) || (obj instanceof java.lang.Long) || (obj instanceof java.lang.Float) || (obj instanceof java.lang.Double);
        else
        if(c1._fldbyte == 12)
            flag = (obj instanceof java.lang.Boolean) || (obj instanceof java.lang.Byte) || (obj instanceof java.lang.Short) || (obj instanceof java.lang.Integer) || (obj instanceof java.lang.Long);
        else
        if(c1._fldbyte == 13)
            flag = (obj instanceof java.lang.Boolean) || (obj instanceof java.lang.Character) || (obj instanceof java.lang.Byte) || (obj instanceof java.lang.Short) || (obj instanceof java.lang.Integer) || (obj instanceof java.lang.Long);
        else
        if(c1._fldbyte == 14)
            flag = (obj instanceof java.lang.Byte) || (obj instanceof java.lang.Short) || (obj instanceof java.lang.Integer);
        if(!flag)
        {
            java.lang.String s = obj.getClass().getName();
            throw new ClassCastException("One of the arguments can not be casted to the necessary data type as required by one of the format flags: %" + c1.a() + " != " + s + ".");
        } else
        {
            return;
        }
    }

    private static int a(printf.c c1)
    {
        return !c1.i ? 3 : 1;
    }

    private static boolean _mthif(printf.c c1)
    {
        return c1.o == '0';
    }

    private static int _mthdo(printf.c c1)
    {
        if(c1._fldlong)
            return 8;
        return !c1.p ? 4 : 12;
    }

    private static double _mthif(java.lang.Object obj)
    {
        if(obj instanceof java.lang.Character)
            return (double)((java.lang.Character)obj).charValue();
        else
            return ((java.lang.Number)obj).doubleValue();
    }

    private static long a(java.lang.Object obj)
    {
        if(obj instanceof java.lang.Character)
            return (long)((java.lang.Character)obj).charValue();
        if(obj instanceof java.lang.Boolean)
            return !((java.lang.Boolean)obj).booleanValue() ? 0L : 1L;
        else
            return ((java.lang.Number)obj).longValue();
    }

    private static java.lang.String a(java.lang.Object obj, printf.c c1, boolean flag)
    {
        double d = printf.Format._mthif(obj);
        if(c1.m == 0x80000000)
            if(obj instanceof java.lang.Float)
                c1.m = 6;
            else
            if(obj instanceof java.lang.Double)
                c1.m = 6;
            else
                c1.m = 0;
        int i = printf.Format.a(c1);
        java.lang.String s;
        if(flag || c1.e)
        {
            if(c1.e)
            {
                long l = (long)java.lang.Math.abs(d);
                if(l != 0L)
                    c1.m -= ((int) ((long)(java.lang.Math.log(l) / _fldvoid)));
            }
            if(i == 1)
                s = printf.a.a(d, c1.g, c1.m, i, printf.Format._mthif(c1), printf.Format._mthdo(c1));
            else
                s = printf.a.a(d, c1.g, c1.m, i, printf.Format._mthif(c1), printf.Format._mthdo(c1));
        } else
        {
            long l1 = (long)java.lang.Math.abs(d);
            if(i == 1)
                s = printf.a.a(d, c1.g, c1.m, i, printf.Format._mthif(c1), printf.Format._mthdo(c1));
            else
                s = printf.a.a(d, c1.g, c1.m, i, printf.Format._mthif(c1), printf.Format._mthdo(c1));
            int j = s.length() - 1;
            int k = j;
            char c2;
            for(c2 = s.charAt(k); c2 == '0'; c2 = s.charAt(--k));
            if(c2 == '.')
                k--;
            if(k != j)
                s = s.substring(0, k + 1);
            int i1 = c1.g - s.length();
            if(i1 > 0)
            {
                java.lang.String s1;
                for(s1 = ""; i1-- > 0; s1 = s1 + " ");
                if(i == 1)
                    s = s + s1;
                else
                    s = s1 + s;
            }
        }
        return s;
    }

    private static long a(double d)
    {
        long l;
        if(d < 0.0D)
        {
            l = (long)(java.lang.Math.log(-d) / _fldvoid);
            if(l < 0L)
                l--;
            else
            if(-1D < d && d < 0.0D)
                l--;
        } else
        if(d > 0.0D)
        {
            l = (long)(java.lang.Math.log(d) / _fldvoid);
            if(l < 0L)
                l--;
            else
            if(0.0D < d && d < 1.0D)
                l--;
        } else
        {
            l = 0L;
        }
        return l;
    }

    private static double a(double d, long l)
    {
        return d / java.lang.Math.pow(10D, l);
    }

    private static java.lang.String _mthdo(java.lang.Object obj, printf.c c1)
    {
        double d = printf.Format._mthif(obj);
        long l = printf.Format.a(d);
        if(c1.m == 0x80000000)
            if(obj instanceof java.lang.Float)
                c1.m = 6;
            else
            if(obj instanceof java.lang.Double)
                c1.m = 6;
            else
                c1.m = 0;
        double d1 = printf.Format.a(d, l);
        double d2 = d1 >= 0.0D ? d1 : -d1;
        if((int)d2 == 9)
        {
            byte abyte0[] = printf.a._mthdo(d2, c1.m);
            if(printf.a.a(abyte0, c1.m) > 0)
            {
                l++;
                d1 = printf.Format.a(d, l);
            }
            abyte0 = null;
        } else
        if((int)d2 == 10)
        {
            d1 /= 10D;
            l++;
        }
        return printf.Format.a(d1, l, c1, true);
    }

    private static java.lang.String a(double d, long l, printf.c c1, boolean flag)
    {
        java.lang.String s = java.lang.String.valueOf(java.lang.Math.abs(l));
        int i = s.length();
        if(i == 1)
        {
            s = "0" + s;
            i = 2;
        }
        if(l < 0L)
            s = "-" + s;
        else
            s = "+" + s;
        i++;
        s = "e" + s;
        i++;
        if(c1.g >= i)
            c1.g -= i;
        int j = printf.Format.a(c1);
        java.lang.String s1;
        if(flag || c1.e)
        {
            if(c1.e)
                c1.m--;
            if(j != 1)
            {
                s1 = printf.a.a(d, c1.g, c1.m, j, printf.Format._mthif(c1), printf.Format._mthdo(c1));
                s1 = s1 + s;
            } else
            {
                s1 = printf.a.a(d, 0, c1.m, j, printf.Format._mthif(c1), printf.Format._mthdo(c1));
                int k = c1.g - s1.length();
                for(s1 = s1 + s; k-- > 0; s1 = s1 + " ");
            }
        } else
        {
            if(j != 1)
                s1 = printf.a.a(d, c1.g, c1.m - 1, j, printf.Format._mthif(c1), printf.Format._mthdo(c1));
            else
                s1 = printf.a.a(d, 0, c1.m - 1, j, printf.Format._mthif(c1), printf.Format._mthdo(c1));
            int i1 = s1.length() - 1;
            int j1 = i1;
            char c2;
            for(c2 = s1.charAt(j1); c2 == '0'; c2 = s1.charAt(--j1));
            if(c2 == '.')
                j1--;
            if(j1 != i1)
                s1 = s1.substring(0, j1 + 1);
            s1 = s1 + s;
            int k1 = (c1.g + i) - s1.length();
            if(k1 > 0)
            {
                java.lang.String s2;
                for(s2 = ""; k1-- > 0; s2 = s2 + " ");
                if(j == 1)
                    s1 = s1 + s2;
                else
                    s1 = s2 + s1;
            }
        }
        return s1;
    }

    private static java.lang.String _mthfor(java.lang.Object obj, printf.c c1)
    {
        double d = printf.Format._mthif(obj);
        if(c1.m == 0x80000000)
            if(obj instanceof java.lang.Float)
                c1.m = 6;
            else
            if(obj instanceof java.lang.Double)
                c1.m = 6;
            else
                c1.m = 0;
        long l = printf.Format.a(d);
        double d1 = printf.Format.a(d, l);
        double d2 = d1 >= 0.0D ? d1 : -d1;
        if((int)d2 == 9)
        {
            byte abyte0[] = printf.a._mthdo(d2, c1.m);
            if(printf.a.a(abyte0, c1.m - 1) > 0)
            {
                l++;
                d1 = printf.Format.a(d, l);
            }
            abyte0 = null;
        } else
        if((int)d2 == 10)
        {
            d1 /= 10D;
            l++;
        }
        if(l < -4L || l >= (long)c1.m)
        {
            return printf.Format.a(d1, l, c1, false);
        } else
        {
            c1.m -= ((int) (l + 1L));
            return printf.Format.a(obj, c1, false);
        }
    }

    private static java.lang.String _mthif(java.lang.Object obj, printf.c c1)
    {
        boolean flag = printf.Format._mthif(c1);
        long l;
        if(obj instanceof java.lang.Character)
        {
            l = ((java.lang.Character)obj).charValue();
            java.lang.String s = java.lang.Long.toHexString(l);
            if(l < 0L)
                l &= 65535L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 16;
                else
                    c1.g = 18;
        } else
        if(obj instanceof java.lang.Byte)
        {
            l = (byte)((java.lang.Number)obj).intValue();
            if(l < 0L)
                l &= 255L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 8;
                else
                    c1.g = 10;
        } else
        if(obj instanceof java.lang.Short)
        {
            l = (short)((java.lang.Number)obj).intValue();
            if(l < 0L)
                l &= 65535L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 16;
                else
                    c1.g = 18;
        } else
        if(obj instanceof java.lang.Integer)
        {
            l = ((java.lang.Integer)obj).intValue();
            if(l < 0L)
                l &= 0xffffffffL;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 32;
                else
                    c1.g = 34;
        } else
        {
            l = ((java.lang.Number)obj).longValue();
            if(l >= 0L && flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 64;
                else
                    c1.g = 66;
        }
        if(c1.g == 0x80000000)
            c1.g = 0;
        if(c1.m == 0x80000000)
            c1.m = 0;
        if(c1.e)
        {
            c1.m -= 2;
            if(c1.g > 2)
                c1.g -= 2;
        }
        java.lang.String s1 = java.lang.Long.toBinaryString(l);
        if(flag)
        {
            if(l < 0L)
                s1 = printf.a.a(s1, c1.g, 0, printf.Format.a(c1), '1', ' ');
            else
                s1 = printf.a.a(s1, c1.g, 0, printf.Format.a(c1), '0', ' ');
        } else
        {
            s1 = printf.a.a(s1, c1.g, 0, printf.Format.a(c1), ' ', ' ');
        }
        if(c1.e)
            s1 = "0b" + s1;
        return s1;
    }

    private static java.lang.String _mthnew(java.lang.Object obj, printf.c c1)
    {
        boolean flag = printf.Format._mthif(c1);
        long l;
        if(obj instanceof java.lang.Character)
        {
            l = ((java.lang.Character)obj).charValue();
            java.lang.String s = java.lang.Long.toHexString(l);
            if(l < 0L)
                l &= 65535L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 4;
                else
                    c1.g = 6;
        } else
        if(obj instanceof java.lang.Byte)
        {
            l = (byte)((java.lang.Number)obj).intValue();
            if(l < 0L)
                l &= 255L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 2;
                else
                    c1.g = 4;
        } else
        if(obj instanceof java.lang.Short)
        {
            l = (short)((java.lang.Number)obj).intValue();
            if(l < 0L)
                l &= 65535L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 4;
                else
                    c1.g = 6;
        } else
        if(obj instanceof java.lang.Integer)
        {
            l = ((java.lang.Integer)obj).intValue();
            if(l < 0L)
                l &= 0xffffffffL;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 8;
                else
                    c1.g = 10;
        } else
        {
            l = ((java.lang.Number)obj).longValue();
            if(l >= 0L && flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 16;
                else
                    c1.g = 18;
        }
        if(c1.g == 0x80000000)
            c1.g = 0;
        if(c1.e)
        {
            c1.m -= 2;
            if(c1.g > 2)
                c1.g -= 2;
        }
        java.lang.String s1 = java.lang.Long.toHexString(l);
        if(flag)
        {
            if(l < 0L)
                s1 = printf.a.a(s1, c1.g, 0, printf.Format.a(c1), 'f', ' ');
            else
                s1 = printf.a.a(s1, c1.g, 0, printf.Format.a(c1), '0', ' ');
        } else
        {
            s1 = printf.a.a(s1, c1.g, 0, printf.Format.a(c1), ' ', ' ');
        }
        if(c1.e)
            s1 = "0x" + s1;
        return s1;
    }

    private static java.lang.String a(java.lang.Object obj, printf.c c1)
    {
        boolean flag = printf.Format._mthif(c1);
        long l;
        if(obj instanceof java.lang.Character)
        {
            l = ((java.lang.Character)obj).charValue();
            if(l < 0L)
                l &= 65535L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 6;
                else
                    c1.g = 7;
        } else
        if(obj instanceof java.lang.Byte)
        {
            l = (byte)((java.lang.Number)obj).intValue();
            if(l < 0L)
                l &= 255L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 3;
                else
                    c1.g = 4;
        } else
        if(obj instanceof java.lang.Short)
        {
            l = (short)((java.lang.Number)obj).intValue();
            if(l < 0L)
                l &= 65535L;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 6;
                else
                    c1.g = 7;
        } else
        if(obj instanceof java.lang.Integer)
        {
            l = ((java.lang.Integer)obj).longValue();
            if(l < 0L)
                l &= 0xffffffffL;
            else
            if(flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 11;
                else
                    c1.g = 12;
        } else
        {
            l = ((java.lang.Long)obj).longValue();
            if(l >= 0L && flag && c1.g == 0x80000000)
                if(!c1.e)
                    c1.g = 22;
                else
                    c1.g = 23;
        }
        if(c1.g == 0x80000000)
            c1.g = 0;
        if(c1.e)
        {
            c1.m--;
            if(c1.g > 1)
                c1.g--;
        }
        java.lang.String s = java.lang.Long.toOctalString(l);
        if(flag)
        {
            if(l < 0L)
                s = printf.a.a(s, c1.g, 0, printf.Format.a(c1), '7', ' ');
            else
                s = printf.a.a(s, c1.g, 0, printf.Format.a(c1), '0', ' ');
        } else
        {
            s = printf.a.a(s, c1.g, 0, printf.Format.a(c1), ' ', ' ');
        }
        if(c1.e)
            s = "0" + s;
        return s;
    }

    private static java.lang.String _mthint(java.lang.Object obj, printf.c c1)
    {
        long l;
        if(obj instanceof java.lang.Character)
            l = ((java.lang.Character)obj).charValue();
        else
            l = ((java.lang.Number)obj).longValue();
        if(l < 0L)
            l &= 65535L;
        if(c1.g == 0x80000000)
            c1.g = 0;
        java.lang.String s = java.lang.Long.toHexString(l);
        int i = s.length();
        if(i > 4)
            s = s.substring(i - 3);
        else
        if(i < 4)
            s = "0000".substring(4 - i) + s;
        s = "\\u" + s;
        s = printf.a.a(s, c1.g, 0, printf.Format.a(c1), c1.o, c1.o);
        return s;
    }

    public static void setModifyLineSeparators(boolean flag)
    {
        _fldlong = flag;
    }

    public static boolean getModifyLineSeparators()
    {
        return _fldlong;
    }

    public static java.lang.String sprintf(java.lang.String s, java.util.Vector vector)
        throws printf.ParseErrorException
    {
        java.lang.String s3;
        synchronized(_fldnull)
        {
            java.lang.String s1 = "";
            boolean flag = false;
            if(vector.size() > 0 && (vector.firstElement() instanceof printf.ParametersAutoClear))
            {
                flag = true;
                vector.removeElementAt(0);
            }
            printf.b b1 = new b(s);
            b1._mthelse();
            java.util.Vector vector1 = b1._mthbyte();
            for(int i = 0; i < vector1.size(); i++)
            {
                printf.c c1 = (printf.c)vector1.elementAt(i);
                java.lang.Object obj1 = vector.elementAt(i);
                if(c1.g == -1)
                {
                    c1.g = ((java.lang.Number)obj1).intValue();
                    if(c1.g < 0)
                    {
                        c1.i = true;
                        c1.g = -c1.g;
                    }
                    vector.removeElementAt(i);
                }
                obj1 = vector.elementAt(i);
                if(c1.m == -1)
                {
                    c1.m = ((java.lang.Number)obj1).intValue();
                    vector.removeElementAt(i);
                }
            }

            java.util.Vector vector2 = b1._mthchar();
            for(int j = 0; j < vector1.size(); j++)
            {
                printf.c c2 = (printf.c)vector1.elementAt(j);
                int k = printf.Format.a(c2);
                boolean flag1 = printf.Format._mthif(c2);
                int l = printf.Format._mthdo(c2);
                java.lang.String s4 = (java.lang.String)vector2.elementAt(j);
                java.lang.Object obj2 = vector.elementAt(j);
                printf.Format.a(c2, obj2);
                s1 = s1 + s4;
                java.lang.String s2 = "";
                if(c2._fldbyte == 11)
                {
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    if(obj2 == null)
                        s2 = printf.a.a("null", c2.g, c2.m, printf.Format.a(c2), c2.o, c2.o);
                    else
                    if(obj2 instanceof java.lang.String)
                        s2 = printf.a.a((java.lang.String)obj2, c2.g, c2.m, printf.Format.a(c2), c2.o, c2.o);
                    else
                        s2 = printf.a.a(obj2.toString(), c2.g, c2.m, printf.Format.a(c2), c2.o, c2.o);
                } else
                if(c2._fldbyte == 12)
                {
                    boolean flag2;
                    if(obj2 instanceof java.lang.Boolean)
                        flag2 = ((java.lang.Boolean)obj2).booleanValue();
                    else
                        flag2 = ((java.lang.Number)obj2).longValue() != 0L;
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    s2 = printf.a.a(flag2, c2.g, printf.Format.a(c2), c2.m);
                } else
                if(c2._fldbyte == 1)
                {
                    char c3;
                    if(obj2 instanceof java.lang.Character)
                        c3 = ((java.lang.Character)obj2).charValue();
                    else
                        c3 = (char)((java.lang.Number)obj2).intValue();
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    if(!c2.e)
                    {
                        if(c2.g == 0x80000000)
                            c2.g = 0;
                        s2 = printf.a.a(c3, c2.g, printf.Format.a(c2), c2.o, c2.o);
                    } else
                    {
                        s2 = printf.Format._mthint(obj2, c2);
                    }
                } else
                if(c2._fldbyte == 2)
                {
                    long l1;
                    if(obj2 instanceof java.lang.Character)
                        l1 = ((java.lang.Character)obj2).charValue();
                    else
                        l1 = ((java.lang.Number)obj2).longValue();
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    s2 = printf.a.a(l1, c2.g, printf.Format.a(c2), printf.Format._mthif(c2), printf.Format._mthdo(c2));
                } else
                if(c2._fldbyte == 13)
                    s2 = printf.Format._mthif(obj2, c2);
                else
                if(c2._fldbyte == 4)
                {
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    s2 = printf.Format._mthnew(obj2, c2);
                } else
                if(c2._fldbyte == 3)
                {
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    s2 = printf.Format.a(obj2, c2);
                } else
                if(c2._fldbyte == 8)
                {
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    s2 = printf.Format.a(obj2, c2, true);
                } else
                if(c2._fldbyte == 6)
                {
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    s2 = printf.Format._mthdo(obj2, c2);
                } else
                if(c2._fldbyte == 9)
                {
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    s2 = printf.Format._mthfor(obj2, c2);
                } else
                if(c2._fldbyte == 14)
                {
                    long l2;
                    if(obj2 instanceof java.lang.Character)
                        l2 = ((java.lang.Character)obj2).charValue();
                    else
                    if(obj2 instanceof java.lang.Byte)
                    {
                        l2 = ((java.lang.Byte)obj2).longValue();
                        if(l2 < 0L)
                            l2 += 256L;
                    } else
                    if(obj2 instanceof java.lang.Short)
                    {
                        l2 = ((java.lang.Short)obj2).longValue();
                        if(l2 < 0L)
                            l2 += 0x10000L;
                    } else
                    if(obj2 instanceof java.lang.Integer)
                    {
                        l2 = ((java.lang.Integer)obj2).longValue();
                        if(l2 < 0L)
                            l2 += 0x100000000L;
                    } else
                    {
                        if(obj2 instanceof java.lang.Long)
                        {
                            l2 = ((java.lang.Long)obj2).longValue();
                            throw new ClassCastException("Convertion of a long into an unsigned long is not supported: " + l2 + "l");
                        }
                        l2 = ((java.lang.Number)obj2).intValue();
                        if(l2 < 0L)
                            l2 += 0x100000000L;
                    }
                    if(c2.g == 0x80000000)
                        c2.g = 0;
                    if(c2.m == 0x80000000)
                        c2.m = 0;
                    s2 = printf.a.a(l2, c2.g, printf.Format.a(c2), printf.Format._mthif(c2), printf.Format._mthdo(c2));
                } else
                {
                    java.lang.System.err.println("sprintf(): Unknown type " + c2.a());
                }
                if(c2._fldelse == 2)
                    s2 = s2.toUpperCase();
                s1 = s1 + s2;
            }

            s1 = s1 + (java.lang.String)vector2.lastElement();
            if(vector.size() > 0 && (vector.firstElement() instanceof printf.ParametersAutoClear))
                vector.removeAllElements();
            if(flag)
            {
                vector.removeAllElements();
                vector.addElement(((java.lang.Object) (new ParametersAutoClear())));
            }
            if(_fldlong)
                s1 = printf.a.a(s1);
            s3 = s1.toString();
        }
        return s3;
    }

    public static java.lang.String sprintf(java.lang.String s, java.lang.Object aobj[])
        throws printf.ParseErrorException
    {
        java.util.Vector vector = new Vector();
        for(int i = 0; i < aobj.length; i++)
            vector.addElement(aobj[i]);

        java.lang.String s1 = printf.Format.sprintf(s, vector);
        vector.removeAllElements();
        vector = null;
        return s1;
    }

    public static java.lang.String sprintf(java.lang.String s, printf.Parameters parameters)
        throws printf.ParseErrorException
    {
        return printf.Format.sprintf(s, parameters.toVector());
    }

    public static void fprintf(java.io.OutputStream outputstream, java.lang.String s, java.util.Vector vector)
        throws java.io.IOException, printf.ParseErrorException
    {
        java.io.StringReader stringreader = new StringReader(printf.Format.sprintf(s, vector));
        for(int i = stringreader.read(); i != -1; i = stringreader.read())
            outputstream.write(i);

        outputstream.flush();
        stringreader.close();
    }

    public static void fprintf(java.io.OutputStream outputstream, java.lang.String s, printf.Parameters parameters)
        throws java.io.IOException, printf.ParseErrorException
    {
        printf.Format.fprintf(outputstream, s, parameters.toVector());
    }

    public static void fprintf(java.io.OutputStream outputstream, java.lang.String s, java.lang.Object aobj[])
        throws java.io.IOException, printf.ParseErrorException
    {
        java.io.StringReader stringreader = new StringReader(printf.Format.sprintf(s, aobj));
        for(int i = stringreader.read(); i != -1; i = stringreader.read())
            outputstream.write(i);

        outputstream.flush();
        stringreader.close();
    }

    public static void fprintf(java.io.OutputStream outputstream, java.lang.String s)
        throws java.io.IOException, printf.ParseErrorException
    {
        printf.Format.fprintf(outputstream, s, new Vector());
    }

    public static void printf(java.lang.String s, java.util.Vector vector)
        throws printf.ParseErrorException
    {
        java.lang.System.out.print(printf.Format.sprintf(s, vector));
    }

    public static void printf(java.lang.String s, printf.Parameters parameters)
        throws printf.ParseErrorException
    {
        java.lang.System.out.print(printf.Format.sprintf(s, parameters));
    }

    public static void printf(java.lang.String s, java.lang.Object aobj[])
        throws printf.ParseErrorException
    {
        java.lang.System.out.print(printf.Format.sprintf(s, aobj));
    }

    public static void printf(java.lang.String s)
        throws printf.ParseErrorException
    {
        printf.Format.printf(s, new Vector());
    }

    public static void fprintf(java.io.Writer writer, java.lang.String s, java.util.Vector vector)
        throws java.io.IOException, printf.ParseErrorException
    {
        java.io.StringReader stringreader = new StringReader(printf.Format.sprintf(s, vector));
        for(int i = stringreader.read(); i != -1; i = stringreader.read())
            writer.write(i);

        writer.flush();
        stringreader.close();
    }

    public static void fprintf(java.io.Writer writer, java.lang.String s, java.lang.Object aobj[])
        throws java.io.IOException, printf.ParseErrorException
    {
        java.io.StringReader stringreader = new StringReader(printf.Format.sprintf(s, aobj));
        for(int i = stringreader.read(); i != -1; i = stringreader.read())
            writer.write(i);

        writer.flush();
        stringreader.close();
    }

    public static void fprintf(java.io.Writer writer, java.lang.String s, printf.Parameters parameters)
        throws java.io.IOException, printf.ParseErrorException
    {
        printf.Format.fprintf(writer, s, parameters.toVector());
    }

    public static void fprintf(java.io.Writer writer, java.lang.String s)
        throws java.io.IOException, printf.ParseErrorException
    {
        printf.Format.fprintf(writer, s, new Vector());
    }

    public static void main(java.lang.String args[])
        throws java.lang.Exception
    {
        if(args.length == 0 || args[0].equals("-help"))
            java.lang.System.out.println(printf.Format._mthif());
        else
        if(args[0].equals("-printf"))
        {
            int i = 1;
            printf.Format.printf(args[i], printf.Format.a(args, i));
        } else
        if(args[0].equals("-version"))
            printf.Format.version();
    }

    protected static printf.Parameters a(java.lang.String as[], int i)
        throws java.lang.Exception
    {
        printf.Parameters parameters = new Parameters();
        printf.b b1 = new b(as[i]);
        b1._mthelse();
        java.util.Vector vector = b1._mthbyte();
        for(int j = 0; j < vector.size(); j++)
        {
            printf.c c1 = (printf.c)vector.elementAt(j);
            java.lang.String s = as[j + i + 1];
            if(c1.g == -1)
            {
                parameters.add(((java.lang.Object) (java.lang.Long.valueOf(s))));
                i++;
            }
            if(c1.m == -1)
            {
                parameters.add(((java.lang.Object) (java.lang.Long.valueOf(s))));
                i++;
            }
            if(c1._fldbyte == 12)
                try
                {
                    parameters.add(((java.lang.Object) (java.lang.Integer.valueOf(s))));
                }
                catch(java.lang.NumberFormatException numberformatexception)
                {
                    parameters.add(((java.lang.Object) (java.lang.Boolean.valueOf(s))));
                }
            else
            if(c1._fldbyte == 1)
                try
                {
                    if(s.length() != 1)
                        parameters.add(((java.lang.Object) (java.lang.Double.valueOf(s))));
                    else
                        parameters.add(s.charAt(0));
                }
                catch(java.lang.NumberFormatException numberformatexception1)
                {
                    parameters.add(s.charAt(0));
                }
            else
            if(c1._fldbyte == 4 || c1._fldbyte == 3 || c1._fldbyte == 13)
                parameters.add(((java.lang.Object) (java.lang.Long.valueOf(s))));
            else
            if(c1._fldbyte == 8 || c1._fldbyte == 6 || c1._fldbyte == 9)
                parameters.add(((java.lang.Object) (java.lang.Double.valueOf(s))));
            else
            if(c1._fldbyte == 2 || c1._fldbyte == 14)
            {
                int k = s.length();
                char c2 = s.charAt(k - 1);
                try
                {
                    s = s.substring(0, k - 1);
                    if(c2 == 'b')
                        parameters.add(((java.lang.Object) (java.lang.Byte.valueOf(s))));
                    else
                    if(c2 == 's')
                        parameters.add(((java.lang.Object) (java.lang.Short.valueOf(s))));
                    else
                    if(c2 == 'i')
                        parameters.add(((java.lang.Object) (java.lang.Integer.valueOf(s))));
                    else
                    if(c2 == 'l')
                    {
                        parameters.add(((java.lang.Object) (java.lang.Long.valueOf(s))));
                    } else
                    {
                        s = s + c2;
                        try
                        {
                            parameters.add(((java.lang.Object) (java.lang.Integer.valueOf(s))));
                        }
                        catch(java.lang.NumberFormatException numberformatexception2)
                        {
                            parameters.add(((java.lang.Object) (java.lang.Long.valueOf(s))));
                        }
                    }
                }
                catch(java.lang.NumberFormatException numberformatexception3)
                {
                    try
                    {
                        parameters.add(((java.lang.Object) (java.lang.Long.valueOf(s))));
                    }
                    catch(java.lang.NumberFormatException numberformatexception4)
                    {
                        parameters.add(((java.lang.Object) (java.lang.Double.valueOf(s))));
                    }
                }
            } else
            {
                parameters.add(s);
            }
        }

        return parameters;
    }

    public static java.lang.String getVersion()
    {
        java.lang.String s = "1.6";
        return s;
    }

    protected static java.lang.String _mthif()
    {
        return "Java printf version " + printf.Format.getVersion() + " by Henrik Bengtsson. Copyright 1997-2003\n" + "\n" + "Usage:\n" + " java printf.Format [-help|-version|-printf fmtstr [param]*]\n" + "\n" + "Options:\n" + " -help    Print this message.\n" + " -printf  Using the next argument (fmtstr) as the format string and\n" + "          one or more optional parameters (param) as values to be\n" + "          formatted by the printf method.\n" + " -version Print version information.\n" + "\n" + "Example:\n" + " java printf.Format -printf \"Yeah, %d%% %s.\" 100 Java\n" + "prints\n" + " Yeah, 100% Java.\n" + "\n" + "For more information see www.braju.com.\n";
    }

    private static boolean _fldlong = true;
    private static java.lang.Object _fldnull = new Object();
    private static final double _fldvoid = java.lang.Math.log(10D);

}


class c
{

    public c()
    {
        _fldbyte = 0;
        g = 0x80000000;
        m = 0x80000000;
        o = ' ';
        i = false;
        _fldlong = false;
        p = false;
        e = false;
        _fldgoto = false;
        _fldelse = 0;
    }

    public java.lang.String a()
    {
        java.lang.String s1;
        switch(_fldbyte)
        {
        case 0: // '\0'
            s1 = "unknown";
            break;

        case 1: // '\001'
            s1 = "c";
            break;

        case 2: // '\002'
            s1 = "d";
            break;

        case 14: // '\016'
            s1 = "u";
            break;

        case 3: // '\003'
            s1 = "o";
            break;

        case 4: // '\004'
            s1 = "x";
            break;

        case 6: // '\006'
            s1 = "e";
            break;

        case 8: // '\b'
            s1 = "f";
            break;

        case 9: // '\t'
            s1 = "g";
            break;

        case 11: // '\013'
            s1 = "s";
            break;

        case 12: // '\f'
            s1 = "l";
            break;

        case 13: // '\r'
            s1 = "b";
            break;

        case 5: // '\005'
        case 7: // '\007'
        case 10: // '\n'
        default:
            s1 = "error";
            break;
        }
        if(_fldelse == 2)
            s1 = s1.toUpperCase();
        return s1;
    }

    public java.lang.String _mthif()
    {
        java.lang.String s1;
        switch(g)
        {
        case -2147483648: 
            s1 = "UNSPECIFIED";
            break;

        case -1: 
            s1 = "*";
            break;

        case 0: // '\0'
            s1 = "DEFAULT";
            break;

        default:
            s1 = java.lang.String.valueOf(g);
            break;
        }
        return s1;
    }

    public java.lang.String _mthdo()
    {
        java.lang.String s1;
        switch(m)
        {
        case -2147483648: 
            s1 = "UNSPECIFIED";
            break;

        case -1: 
            s1 = "*";
            break;

        case 0: // '\0'
            s1 = "DEFAULT";
            break;

        default:
            s1 = java.lang.String.valueOf(m);
            break;
        }
        return s1;
    }

    public java.lang.String toString()
    {
        return new String("type=" + a() + ", minWidth=" + _mthif() + ", precision=" + _mthdo());
    }

    public int _fldbyte;
    public int g;
    public int m;
    public int _fldelse;
    public char o;
    public boolean i;
    public boolean _fldlong;
    public boolean p;
    public boolean e;
    public boolean _fldgoto;
    public static final int _fldnew = 0;
    public static final int _fldvoid = 1;
    public static final int u = 2;
    public static final int r = 3;
    public static final int d = 4;
    public static final int _fldint = 6;
    public static final int s = 8;
    public static final int f = 9;
    public static final int _fldfor = 11;
    public static final int k = 12;
    public static final int _flddo = 13;
    public static final int _fldcase = 14;
    public static final int j = 0;
    public static final int _fldchar = 1;
    public static final int _fldif = 2;
    public static final int n = 0x80000000;
    public static final int a = -1;
    public static final int _fldtry = 0;
    public static final int c = 0x80000000;
    public static final int h = -1;
    public static final int b = 0;
    public static final int q = 0;
    public static final int t = 1;
    public static final int _fldnull = 2;
    public static final int l = 3;
}



class b
{

    public b(java.lang.String s)
    {
        a = s.toCharArray();
        _fldnew = 0;
        _fldint = new Vector();
        _fldfor = new Vector();
    }

    public java.util.Vector _mthbyte()
    {
        return _fldint;
    }

    public java.util.Vector _mthchar()
    {
        return _fldfor;
    }

    public void _mthelse()
        throws ParseErrorException
    {
        java.lang.String s = new String();
        for(boolean flag = false; !flag;)
            if(a(37))
            {
                if(a(37))
                {
                    s = s + '%';
                } else
                {
                    _fldif = new c();
                    _mthnull();
                    if(!_flddo)
                    {
                        _fldint.addElement(((java.lang.Object) (_fldif)));
                        _fldfor.addElement(((java.lang.Object) (s)));
                        s = new String();
                        _fldif = null;
                    }
                }
            } else
            if(_fldnew < a.length)
            {
                s = s + a[_fldnew++];
            } else
            {
                _fldfor.addElement(((java.lang.Object) (s)));
                flag = true;
            }

    }

    private void _mthfor()
        throws ParseErrorException
    {
        _flddo = true;
        throw new ParseErrorException("Parse error. Unknown symbol: " + a[_fldnew]);
    }

    private void _mthlong()
    {
    }

    private void _mthnull()
        throws ParseErrorException
    {
        _fldif.m = 0x80000000;
        _flddo = false;
        if(_mthnew())
            _mthtry();
        else
        if(_mthgoto())
            _mthvoid();
        else
        if(a(46))
            _mthcase();
        else
        if(_mthint())
            _mthlong();
        else
            _mthfor();
    }

    private void _mthtry()
        throws ParseErrorException
    {
        if(_mthnew())
            _mthtry();
        else
        if(_mthgoto())
            _mthvoid();
        else
        if(a(46))
            _mthcase();
        else
        if(_mthint())
            _mthlong();
        else
            _mthfor();
    }

    private void _mthvoid()
        throws ParseErrorException
    {
        if(a(46))
            _mthcase();
        else
        if(_mthint())
            _mthlong();
        else
            _mthfor();
    }

    private void _mthif()
        throws ParseErrorException
    {
        if(_mthint())
            _mthlong();
        else
            _mthfor();
    }

    private void _mthcase()
        throws ParseErrorException
    {
        if(_mthdo())
            _mthif();
        else
        if(_mthint())
            _mthlong();
        else
            _mthfor();
    }

    private int a()
    {
        if(_fldnew == a.length)
            return -1;
        int i = -1;
        for(boolean flag = false; !flag;)
            if(java.lang.Character.isDigit(a[_fldnew]))
            {
                if(_fldnew == a.length)
                    i = 0;
                if(i == -1)
                    i = a[_fldnew] - 48;
                else
                    i = 10 * i + (a[_fldnew] - 48);
                if(++_fldnew == a.length)
                    flag = true;
            } else
            {
                flag = true;
            }

        return i;
    }

    private boolean _mthgoto()
    {
        if(a(42))
        {
            _fldif.g = -1;
            return true;
        }
        int i = a();
        if(i != -1)
        {
            _fldif.g = i;
            return true;
        } else
        {
            return false;
        }
    }

    private boolean _mthdo()
    {
        if(a(42))
        {
            _fldif.m = -1;
            return true;
        }
        int i = a();
        if(i != -1)
        {
            _fldif.m = i;
            return true;
        } else
        {
            return false;
        }
    }

    private boolean a(int i)
    {
        if(_fldnew < a.length && a[_fldnew] == i)
        {
            _fldnew++;
            return true;
        } else
        {
            return false;
        }
    }

    private boolean _mthnew()
    {
        boolean flag = true;
        if(a(48))
            _fldif.o = '0';
        else
        if(a(43))
            _fldif._fldlong = true;
        else
        if(a(45))
            _fldif.i = true;
        else
        if(a(32))
            _fldif.p = true;
        else
        if(a(35))
            _fldif.e = true;
        else
        if(a(39))
            _fldif._fldgoto = true;
        else
            flag = false;
        return flag;
    }

    private boolean _mthint()
    {
        boolean flag = true;
        if(a(99))
            _fldif._fldbyte = 1;
        else
        if(a(100) || a(105))
            _fldif._fldbyte = 2;
        else
        if(a(117))
            _fldif._fldbyte = 14;
        else
        if(a(111))
            _fldif._fldbyte = 3;
        else
        if(a(120))
        {
            _fldif._fldbyte = 4;
            _fldif._fldelse = 1;
        } else
        if(a(88))
        {
            _fldif._fldbyte = 4;
            _fldif._fldelse = 2;
        } else
        if(a(101))
        {
            _fldif._fldbyte = 6;
            _fldif._fldelse = 1;
        } else
        if(a(69))
        {
            _fldif._fldbyte = 6;
            _fldif._fldelse = 2;
        } else
        if(a(102))
            _fldif._fldbyte = 8;
        else
        if(a(103))
        {
            _fldif._fldbyte = 9;
            _fldif._fldelse = 1;
        } else
        if(a(71))
        {
            _fldif._fldbyte = 9;
            _fldif._fldelse = 2;
        } else
        if(a(115))
            _fldif._fldbyte = 11;
        else
        if(a(108))
        {
            _fldif._fldbyte = 12;
            _fldif._fldelse = 1;
        } else
        if(a(76))
        {
            _fldif._fldbyte = 12;
            _fldif._fldelse = 2;
        } else
        if(a(98))
        {
            _fldif._fldbyte = 13;
            _fldif._fldelse = 1;
        } else
        if(a(66))
        {
            _fldif._fldbyte = 13;
            _fldif._fldelse = 2;
        } else
        {
            flag = false;
        }
        return flag;
    }

    private char a[];
    private int _fldnew;
    c _fldif;
    java.util.Vector _fldint;
    java.util.Vector _fldfor;
    boolean _flddo;
}

class a
{

    a()
    {
    }

    private static void a()
    {
        try
        {
            java.lang.String s = java.lang.System.getProperties().getProperty("line.separator");
            if(s.length() == 1)
            {
                if(s.charAt(0) == '\r')
                    _fldfor = 1;
                else
                    _fldfor = 2;
            } else
            {
                _fldfor = 3;
            }
        }
        catch(java.lang.SecurityException securityexception)
        {
            _fldfor = 2;
        }
    }

    protected static java.lang.String a(java.lang.String s)
    {
        if(_fldfor == 2)
            return s;
        if(_fldfor == 1)
        {
            java.lang.StringBuffer stringbuffer = new StringBuffer(s);
            for(int i = 0; i < stringbuffer.length(); i++)
                if(stringbuffer.charAt(i) == '\n')
                    stringbuffer.setCharAt(i, '\r');

            return stringbuffer.toString();
        }
        if(_fldfor == 3)
        {
            java.lang.StringBuffer stringbuffer1 = new StringBuffer(s);
            for(int j = 0; j < stringbuffer1.length(); j++)
            {
                char c = stringbuffer1.charAt(j);
                if(c == '\n')
                    stringbuffer1.insert(j++, '\r');
                if(c == '\r' && (++j == stringbuffer1.length() || stringbuffer1.charAt(j) != '\n'))
                    stringbuffer1.insert(j, '\n');
            }

            return stringbuffer1.toString();
        } else
        {
        	printf.a.a();
            return printf.a.a(s);
        }
    }

    static java.lang.String a(int i, char c)
    {
        java.lang.String s = new String();
        for(; i > 0; i--)
            s = s + c;

        return s;
    }

    static boolean _mthif(int i)
    {
        return i == 3 || i == 1 || i == 2;
    }

    static boolean a(int i)
    {
        return i == 4 || i == 8 || i == 12;
    }

    static java.lang.String a(java.lang.String s, int i, int j, int k, char c, char c1)
    {
        if(s == null || i < 0 || j < 0 || !printf.a._mthif(k))
            return null;
        java.lang.String s1;
        if(j > 0)
        {
            int l = s.length();
            if(j > l)
                j = l;
            s1 = s.substring(0, j);
        } else
        {
            s1 = new String(s);
        }
        int i1 = s1.length();
        if(i > i1)
        {
            int j1 = i - i1;
            if(k == 1)
                s1 = s1 + printf.a.a(j1, c1);
            else
            if(k == 3)
                s1 = printf.a.a(j1, c) + s1;
            else
            if(k == 2)
            {
                int k1 = j1 / 2;
                int l1 = j1 - k1;
                s1 = printf.a.a(k1, c) + s1 + printf.a.a(l1, c1);
            }
        }
        return s1;
    }

    static java.lang.String a(char c, int i, int j, char c1, char c2)
    {
        if(i < 0 || !printf.a._mthif(j))
            return null;
        else
            return printf.a.a(java.lang.String.valueOf(c), i, 0, j, c1, c2);
    }

    static char a(long l, int i)
    {
        char c = '\0';
        if(l >= 0L)
        {
            if(i == 8)
                c = '+';
            else
            if(i == 12)
                c = ' ';
        } else
        if(l < 0L)
            c = '-';
        return c;
    }

    static char a(double d, int i)
    {
        char c = '\0';
        if(d >= 0.0D)
        {
            if(i == 8)
                c = '+';
            else
            if(i == 12)
                c = ' ';
        } else
        if(d < 0.0D)
            c = '-';
        return c;
    }

    static char a(int i, int j)
    {
        return printf.a.a(i, j);
    }

    static java.lang.String a(long l, int i, int j, boolean flag, int k)
    {
        if(i < 0 || !printf.a._mthif(j) || !printf.a.a(k))
            return null;
        java.lang.String s;
        if(l < 0L)
        {
            if(l == 0x8000000000000000L)
                s = java.lang.String.valueOf(l).substring(1);
            else
                s = java.lang.String.valueOf(-l);
        } else
        {
            s = java.lang.String.valueOf(l);
        }
        char c = printf.a.a(l, k);
        if(flag)
        {
            if(c != 0)
            {
                if(i > 0)
                    i--;
                s = c + printf.a.a(s, i, 0, j, '0', ' ');
            } else
            {
                s = printf.a.a(s, i, 0, j, '0', ' ');
            }
        } else
        if(c != 0)
            s = printf.a.a(c + s, i, 0, j, ' ', ' ');
        else
            s = printf.a.a(s, i, 0, j, ' ', ' ');
        return s;
    }

    static java.lang.String a(int i, int j, int k, boolean flag, int l)
    {
        return printf.a.a(i, j, k, flag, l);
    }

    static java.lang.String a(double d, int i, int j, int k, boolean flag, int l)
    {
        if(i < 0 || j < 0 || !printf.a._mthif(k) || !printf.a.a(l))
            return null;
        java.lang.String s;
        if(d < 0.0D)
            s = printf.a._mthif(-d, j);
        else
            s = printf.a._mthif(d, j);
        char c = printf.a.a(d, l);
        if(flag)
        {
            if(c != 0)
            {
                if(i > 0)
                    i--;
                s = c + printf.a.a(s, i, 0, k, '0', ' ');
            } else
            {
                s = printf.a.a(s, i, 0, k, '0', ' ');
            }
        } else
        if(c != 0)
            s = printf.a.a(c + s, i, 0, k, ' ', ' ');
        else
            s = printf.a.a(s, i, 0, k, ' ', ' ');
        return s;
    }

    protected static byte[] _mthdo(double d, int i)
    {
        byte abyte0[] = new byte[i + 1];
        long l = 1L;
        long l1 = 10L * (long)d;
        for(int j = 0; j <= i; j++)
        {
            l *= 10L;
            byte byte0 = (byte)(int)(d * (double)l - (double)l1);
            if(byte0 == 10)
            {
                abyte0[j] = 0;
                l1 += 10L;
                for(int k = j - 1; ++abyte0[k] == 10; abyte0[k--] = 0);
            } else
            {
                abyte0[j] = byte0;
            }
            l1 = 10L * (l1 + (long)abyte0[j]);
        }

        return abyte0;
    }

    protected static int a(byte abyte0[], int i)
    {
        if(abyte0[i] >= 5)
        {
            if(i == 0)
                return 1;
            abyte0[i - 1]++;
            for(int j = i - 1; j > 0; j--)
                if(abyte0[j] == 10)
                {
                    abyte0[j] = 0;
                    abyte0[j - 1]++;
                }

            if(abyte0[0] == 10)
            {
                abyte0[0] = 0;
                return 1;
            }
        }
        return 0;
    }

    protected static java.lang.String _mthif(double d, int i)
    {
        long l1 = 1L;
        boolean flag = d < 0.0D;
        if(flag)
            d = -d;
        long l = (long)d;
        byte abyte0[] = printf.a._mthdo(d, i);
        l += printf.a.a(abyte0, i);
        java.lang.StringBuffer stringbuffer = new StringBuffer();
        if(flag)
            stringbuffer.append('-');
        stringbuffer.append(java.lang.String.valueOf(l));
        stringbuffer.append('.');
        for(int j = 0; j < i; j++)
            stringbuffer.append(java.lang.String.valueOf(((int) (abyte0[j]))));

        return stringbuffer.toString();
    }

    static java.lang.String a(boolean flag, int i, int j, int k)
    {
        if(i < 0 || !printf.a._mthif(j))
            return null;
        java.lang.String s;
        if(flag)
            s = "true";
        else
            s = "false";
        s = printf.a.a(s, i, k, j, ' ', ' ');
        return s;
    }

    protected static final int _fldchar = 0;
    protected static final int a = 1;
    protected static final int _fldif = 2;
    protected static final int _fldbyte = 3;
    protected static int _fldfor = 0;
    static final int _fldgoto = 0;
    static final int _fldnew = 1;
    static final int _fldelse = 2;
    static final int _fldint = 3;
    static final int _fldtry = 4;
    static final int _fldcase = 8;
    static final int _flddo = 12;

}
