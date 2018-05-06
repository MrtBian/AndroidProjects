package translation;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Wing on 2017/7/22.
 */

public final class StringTools {
    /* 单词词性最大长度，如addr*/
    private static int MAXLEN = 4;


    public static String repairWord(String word) {
        String temp = word.replace("![a-zA-Z-]", "");
        return temp;
    }

    public static String repairSentence(String sen) {
        String temp = sen.replaceAll("![a-zA-Z'-,.\\[\\]\"()%!@#$^&*_+{}|\\\\;<>/`~]", "*");
        Log.v("原文：",temp);
        temp = temp.replaceAll("-+\n", "");
        temp = temp.replaceAll("\n", " ");
        temp = temp.replaceAll(" +", " ");
        return temp;
    }

    /**
     * @param str
     * @return 返回字符串第一个小写字母的下标
     */
    public static final int reFirst(String str) {
        int index = 0;
        while (index < str.length()) {
            if (str.charAt(index) <= 'z' && str.charAt(index) >= 'a')
                return index;
            index++;
        }
        return -1;
    }

    /**
     * @param str explain
     * @return 按照词性分割单词释义
     */
    public static final String divStr(String str) {
        String reStr = "";
        ArrayList<String> strings = new ArrayList<String>();
        String temp = new String(str);
        int index = reFirst(temp);
        if (index > 0)
            temp = temp.substring(index);
        int tmp = temp.indexOf("[a-z]*\\.");
        if (tmp < 1) {
            return temp;
        } else {
            String temp1 = temp.substring(MAXLEN);
            tmp = temp1.indexOf("[a-z]*\\.");
            if (tmp < 1) {
                return temp;
            }
            return temp.substring(0, index + MAXLEN) + divStr(temp1.substring(index));
        }

//        if (temp.length() < 4)
//            return temp;
//        int flag = reFirst(temp.substring(MAXLEN)) + MAXLEN;//词性最多4位
//
//        while (flag > 3) {
//            //System.out.println(flag+" "+temp);
//            strings.add(temp.substring(0, flag - 1));
//            temp = temp.substring(flag, temp.length());
//            if (temp.length() < MAXLEN) {
//                break;
//            }
//            flag = reFirst(temp.substring(MAXLEN)) + MAXLEN;//词性最多4位
//        }
//        strings.add(temp);
//        for (String s : strings) {
//            reStr += s + "\n";
//        }
//        reStr = reStr.substring(0,reStr.length()-1);//去掉最后一个换行
//        return reStr;
    }

    //unicode 转字符串
    public static String unicodeToZH(String unicode) {
        StringBuffer string = new StringBuffer();
        int data = Integer.parseInt(unicode.substring(2, 6), 16);// 转换出每一个代码点
        string.append((char) data);// 追加成string
        return string.toString();
    }

    //Unicode转中文
    public static String decodeUnicode(final String dataStr) {
        int start = dataStr.indexOf("\\u");
        int end = 0;
        String str = "";
        if (start == -1)
            str = dataStr;
        else {
            str +=dataStr.substring(0, start);
            if (dataStr.length() - start >= 6) {
                str+=unicodeToZH(dataStr.substring(start, start+6)) + decodeUnicode(dataStr.substring(start+6,dataStr.length()));

            }
            else {
                str+=dataStr.substring(start, dataStr.length());
            }
        }
        return str;
    }
}
