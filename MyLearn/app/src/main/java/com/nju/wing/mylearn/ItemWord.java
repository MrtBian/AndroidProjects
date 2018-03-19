package com.nju.wing.mylearn;

/**
 * Created by Wing on 2017/7/24.
 */

public class ItemWord {

    private String word;
    private double grade;
    private int occurtime;

    public ItemWord(String w, double g, int o) {
        word = w;
        grade = g;
        occurtime =o;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String w) {
        word = w;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double g) {
        grade = g;
    }

    public int getOccurtime() {
        return occurtime;
    }

    public void setOccurtime(int o) {
        occurtime = o;
    }
}
