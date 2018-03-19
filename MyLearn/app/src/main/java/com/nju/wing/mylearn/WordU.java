package com.nju.wing.mylearn;

/**
 * Created by Wing on 2017/7/24.
 */

public class WordU {
    //表的各域名
    public static final String KEY_ID = "id";
    public static final String KEY_grade = "GRADE";
    public static final String KEY_occurtime = "OCCURTIME";

    //属性
    public int wordID;
    public double grade;
    public int occurtime;

    public WordU (int id, double g, int time) {
        wordID = id;
        grade = g;
        occurtime = time;
    }
}
