package translation;

import java.util.ArrayList;

/**
 * Created by Wing on 2017/7/22.
 */

public final class StringTools {
    /* 单词词性最大长度，如addr*/
    private static int MAXLEN = 4;

    /**
     * @param str
     * @return 返回字符串第一个小写字母的下标
     */
    public static final int reFirst (String str) {
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
    public static final String divStr (String str) {
        String reStr = "";
        ArrayList<String> strings = new ArrayList<String>();
        String temp = new String(str);
        int index = reFirst(temp);
        if (index > 0)
            temp = temp.substring(index);
        int tmp = temp.indexOf("[a-z]*\\.");
        if(tmp<1){
            return temp;
        }
        else {
            String temp1 = temp.substring(MAXLEN);
            tmp = temp1.indexOf("[a-z]*\\.");
            if(tmp<1){
                return temp;
            }
            return temp.substring(0,index+MAXLEN)+divStr(temp1.substring(index));
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
}
