package com.nju.wing.accounting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wing on 2017/8/6.
 */

public class MessAnalysis {

    private String message;
    private int typ;
    private int cardID;
    private String typestr;
    private double remain;  //余额
    private double money;   //流通金额数值
    private String date;
    MessAnalysis(String m){
        message = m;
        typ = 0;
        int index1,index2;
        if(message.matches(".*?支出.*?")){
            typ = 0;
        }
        if(message.matches(".*?收入.*?")){
            typ = 1;
        }
        index1 = message.indexOf("号");
        index2 = message.indexOf("卡");
        cardID = Integer.parseInt(message.substring(index1+1,index2));
        //message = message.substring(index2+2);
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}");
        //System.out.println(message);
        Matcher matcher = pattern.matcher(message);
        String time = null;
        if(matcher.find())
            time = matcher.group();
        pattern = Pattern.compile("(\\d{1,2}月)?\\d{1,2}日?\\d{2}:\\d{2}");
        //System.out.println(message);
        matcher = pattern.matcher(message);
        if(matcher.find())
            date = matcher.group();
        index1 = message.indexOf(time) + 5;
        index2 = message.indexOf(")") + 1;
        if(index2 == 0)
            index2 = message.indexOf("）") + 1;  //预防中文字符
        typestr = message.substring(index1,index2);
        index1 = index2;
        index2 = message.indexOf("元");
        money = Double.parseDouble(message.substring(index1,index2).replaceAll(",",""));
        index1 = message.indexOf("，余额") + 3;
        index2 = message.indexOf("。") - 1;
        remain = Double.parseDouble(message.substring(index1,index2).replaceAll(",",""));
        System.out.println(cardID+" "+typestr+" "+money+" "+remain+" "+date);

    }

    public int getTyp () {
        return typ;
    }

    public int getCardID () {
        return cardID;
    }

    public String getTypestr () {
        return typestr;
    }

    public double getRemain () {
        return remain;
    }

    public double getMoney () {
        return money;
    }

    public String getDate () {
        return date;
    }

}
