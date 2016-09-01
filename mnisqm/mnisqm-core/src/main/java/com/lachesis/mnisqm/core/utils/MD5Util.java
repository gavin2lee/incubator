package com.lachesis.mnisqm.core.utils;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class MD5Util
{

    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    static final int GB_SP_DIFF = 160;
    static final int[] secPosvalueList = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600 };

    static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z' };

    public static String encrypt(String str)
    {
        String encryptStr = null;
        if (str == null) {
            return null;
        }

        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            encryptStr = hash.toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    public static String filterHtml(String value)
    {
        if (value == null) {
            return null;
        }

        char[] content = new char[value.length()];
        value.getChars(0, value.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);

        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                default:
                    result.append(content[i]);
            }
        }

        return result.toString();
    }

    public static Timestamp getCurrentTime()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String[] splitString(String src, String prefix)
    {
        int pos = 0;
        int size = 10;
        int i = 0;
        int p = 0;
        String[] fix = new String[10];
        while (true) {
            p = src.indexOf(prefix, i);
            if (p == -1) {
                break;
            }
            if (pos == size) {
                size += 10;
                String[] tmp = new String[size];
                System.arraycopy(fix, 0, tmp, 0, pos);
                fix = tmp;
            }
            fix[pos] = src.substring(i, p);
            pos++;
            i = p + prefix.length();
        }
        String[] tmp = new String[pos + 1];
        tmp[pos] = src.substring(i);
        System.arraycopy(fix, 0, tmp, 0, pos);
        return tmp;
    }

    public static Date parseDate(String date)
    {
        return parseDate(date, "yyyy-MM-dd");
    }

    public static Date parseDate(String date, String format)
    {
        if ((date == null) || (date.equals(""))) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = f.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String formatDate(Date date)
    {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String format)
    {
        if (date == null) {
            return "";
        }
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

    public static Date amountDay(Date date, int amount)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, amount);
        return c.getTime();
    }

    public static Date cleanTimeFields(Date d)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static String nullToString(String s)
    {
        return s == null ? "" : s;
    }

    public static String getString(Object o, String defaultStr)
    {
        if (null == o) {
            return defaultStr;
        }
        String ret = (String)o;
        return "".equals(ret) ? defaultStr : ret;
    }

    public static String replace(String str, String pattern, String replace)
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    public static String split2MultiLine(String src, int lineLength)
    {
        int srcLength = src.length();
        if (srcLength <= lineLength) {
            return src;
        }
        StringBuffer sb = new StringBuffer();
        int loopCnt = 0;
        int i = 1;
        while (true)
        {
            if (i * lineLength > srcLength) {
                sb.append(src.substring(loopCnt));
                break;
            }
            sb.append(src.substring(loopCnt, i * lineLength));

            sb.append("��");

            loopCnt = i * lineLength;
            i++;
        }
        return sb.toString();
    }

    public static String truncTableField(String tableField)
    {
        return tableField.substring(tableField.indexOf('.') + 1, tableField.length());
    }

    public static String getGBKString(String src)
    {
        try
        {
            return System.getProperty("user.language").equalsIgnoreCase("zh") ? src : new String(src.getBytes("ISO-8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
        }
        return src;
    }

    public static String getISO8859String(String src)
    {
        try
        {
            return System.getProperty("user.language").equalsIgnoreCase("zh") ? src : new String(src.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return src;
    }

    public static String toEncoded(String url, HttpServletRequest request)
    {
        String sessionId = request.getSession().getId();

        if ((url == null) || (sessionId == null)) {
            return url;
        }

        sessionId = "0000" + sessionId;

        String path = url;
        String query = "";
        String anchor = "";
        int question = url.indexOf('?');
        if (question >= 0) {
            path = url.substring(0, question);
            query = url.substring(question);
        }
        int pound = path.indexOf('#');
        if (pound >= 0) {
            anchor = path.substring(pound);
            path = path.substring(0, pound);
        }
        StringBuffer sb = new StringBuffer(path);
        if (sb.length() > 0) {
            sb.append(";jsessionid=");
            sb.append(sessionId);
        }
        sb.append(anchor);
        sb.append(query);
        return sb.toString();
    }

    public static boolean checkVerifyCode(HttpServletRequest request, String sStr, String tStr)
    {
        String tmp = getVerifyCode(request, sStr);

        return tmp.equals(tStr);
    }

    public static String getVerifyCode(HttpServletRequest request, String sStr)
    {
        String tmp = "";
        HttpSession session = request.getSession(true);

        if (session != null) {
            tmp = session.getId();
            tmp = tmp + sStr;
            tmp = tmp + session.getCreationTime();
        } else {
            tmp = sStr;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(tmp.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            tmp = hash.toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }


}
