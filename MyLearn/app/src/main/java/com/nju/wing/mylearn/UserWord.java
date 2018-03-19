package com.nju.wing.mylearn;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wing on 2017/7/24.
 */

public class UserWord {
    private String BOOKNAME;
    private String TABLENAME;
    private String USERNAME;
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public Cursor cursor;

    public UserWord (Context context, String bookname, String username) {
        dbHelper = new DBHelper(context);
        dbHelper.openDatabase();
        BOOKNAME = bookname;
        USERNAME = username;
        TABLENAME = BOOKNAME + "_user";
        createUserWordTable();
        //createUserInfomationTable();
    }

    protected void finalize () throws java.lang.Throwable {
        dbHelper.close();
        // 递归调用超类中的finalize方法
        super.finalize();
    }
    /**
     * 创建用户表单并初始化
     */
    private void createUserWordTable () {
        db = dbHelper.getWritableDatabase();
        String CreateUesrTable = "create table if not exists " +
                TABLENAME + " (" +
                Word.KEY_ID + " int NOT NULL PRIMARY KEY," +    //WORDID
                "GRADE real," +         //用于单词排序，和difficult相关
                "OCCURTIME int)";       //单词出现次数，初始为零
        db.execSQL(CreateUesrTable);
        String selectQuery = "SELECT " +
                Word.KEY_ID + "," +
                Word.KEY_difficult + " FROM " + BOOKNAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String insert = "insert or ignore into " +
                        TABLENAME + " values(" +
                        cursor.getString(cursor.getColumnIndex(Word.KEY_ID)) + "," +
                        cursor.getString(cursor.getColumnIndex(Word.KEY_difficult)) + ",0);";
                db.execSQL(insert);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /*private void createUserInfomationTable () {
        db = dbHelper.getWritableDatabase();
        String CreateUserTable = "create table if not exists " +
                USERNAME + " (" +
                "SUBJECT varchar(63) NOT NULL PRIMARY KEY)";
        db.execSQL(CreateUserTable);
        String SetCurrentSubject = "insert or ignore into "+
                USERNAME+" values(CET6)";
        db.execSQL(SetCurrentSubject);
        db.close();
    }*/

    /*public void setCurrentSubject(String subject){
        db = dbHelper.getWritableDatabase();
        String SetCurrentSubject = "update "+USERNAME+" SET SUBJECT=" +
                subject + " WHERE " +
                "SUBJECT="+getCurrentSubject();
        db.execSQL(SetCurrentSubject);
        db.close();
    }

    public String getCurrentSubject(){
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT SUBJECT FROM " + USERNAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String subject = cursor.getString(0);
        cursor.close();
        db.close();
        return subject;
    }*/

    /**
     * 获取下一个单词的信息
     * @return 下一个单词
     */
    public Word getNextWord () {
        db = dbHelper.getReadableDatabase();
        String sortByGrade = "SELECT " +
                Word.KEY_ID + " FROM " +
                TABLENAME +
                " ORDER BY GRADE;";
        cursor = db.rawQuery(sortByGrade, null);
        cursor.moveToFirst();
        Word nextWord = new Word();
        nextWord.wordID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Word.KEY_ID)));
        cursor.close();
        String selectWord = "SELECT " +
                Word.KEY_ID + "," +
                Word.KEY_word + "," +
                Word.KEY_explain + "," +
                Word.KEY_ping + "," +
                Word.KEY_difficult +
                " FROM " + BOOKNAME +
                " WHERE " + Word.KEY_ID + "=" + nextWord.wordID +
                " ORDER BY " + Word.KEY_difficult + ";";
        cursor = db.rawQuery(selectWord, null);
        cursor.moveToFirst();
        nextWord.word = cursor.getString(cursor.getColumnIndex(Word.KEY_word));
        nextWord.explain = cursor.getString(cursor.getColumnIndex(Word.KEY_explain));
        nextWord.ping = cursor.getString(cursor.getColumnIndex(Word.KEY_ping));
        nextWord.difficult = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Word.KEY_difficult)));
        cursor.close();
        db.close();
        //System.out.println(nextWord.word);
        return nextWord;
    }

    /**
     * 依靠标志位更新用户表单
     * @param flag   标志位 0 表示记得 1 表示忘记 2 表示熟悉
     * @param wordid 要更新的词
     */
    public void updateGrade (int flag, int wordid) {
        db = dbHelper.getWritableDatabase();
        String getGrade = "SELECT GRADE,OCCURTIME" +
                " FROM " + TABLENAME +
                " WHERE " + WordU.KEY_ID + "=" + wordid;
        cursor = db.rawQuery(getGrade, null);
        cursor.moveToFirst();
        double oldGrade = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WordU.KEY_grade)));
        int oldOccurTime = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_occurtime)));
        cursor.close();
        double newGrade, a = 0;
        int newOccurTime = oldOccurTime + 1;
        switch (flag) {
            case 0:
                a = 0.5;
                break;
            case 1:
                a = 0.1;
                break;
            case 2:
                a = 2;
                break;
        }
        newGrade = oldGrade + a * newOccurTime;
        String updateGrade = "UPDATE " + TABLENAME +
                " SET GRADE=" + newGrade +
                ",OCCURTIME=" + newOccurTime +
                " WHERE " + Word.KEY_ID + "=" + wordid;
        db.execSQL(updateGrade);
        db.close();
    }

    public Map<String, String> getAllWordU () {
        //List<WordU> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        db = dbHelper.getReadableDatabase();
        String sortByGrade = "SELECT *" +
                " FROM " +
                TABLENAME +
                " ORDER BY GRADE limit 100;";
        cursor = db.rawQuery(sortByGrade, null);
        if (cursor.moveToFirst()) {
            do {
                /*WordU word = new WordU(Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_ID))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(WordU.KEY_grade))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_occurtime))));
                list.add(word);*/
                //System.out.println(cursor.getString(0));
                String str1 = cursor.getString(cursor.getColumnIndex(WordU.KEY_ID)),
                        str2 = cursor.getString(cursor.getColumnIndex(WordU.KEY_grade)) +
                                "\n" + cursor.getString(cursor.getColumnIndex(WordU.KEY_occurtime));
                String selectWord = "SELECT " +
                        Word.KEY_word +
                        " FROM " + BOOKNAME +
                        " WHERE " + Word.KEY_ID + "=" + str1 +
                        " ORDER BY " + Word.KEY_difficult + ";";
                Cursor cursor2 = db.rawQuery(selectWord, null);
                if (cursor2.moveToFirst()) {
                    str1 = cursor2.getString(cursor2.getColumnIndex(Word.KEY_word));
                }
                cursor2.close();
                map.put(str1, str2);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return map;
    }

    /**
     * 依照grade对所有单词排序并取前100用于显示
     * @return 前100单词
     */
    public List<Integer> getSortByGrade() {
        db = dbHelper.getReadableDatabase();
        List<Integer> list = new ArrayList<>();
        String sortByGrade = "SELECT " +
                WordU.KEY_ID +
                " FROM " +
                TABLENAME +
                " ORDER BY GRADE limit 100;";
        cursor = db.rawQuery(sortByGrade, null);
        if (cursor.moveToFirst()) {
            do {
                /*WordU word = new WordU(Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_ID))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(WordU.KEY_grade))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_occurtime))));
                list.add(word);*/
                //System.out.println(cursor.getString(0));
                int wordID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_ID)));
                list.add(wordID);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    /**
     * 通过wordid获取单词
     * @param wordid 单词的标号
     * @return 单词
     */
    public String getWord(int wordid){
        db = dbHelper.getWritableDatabase();
        String getGrade = "SELECT " +
                Word.KEY_word +
                " FROM " + BOOKNAME +
                " WHERE " + WordU.KEY_ID + "=" + wordid;
        cursor = db.rawQuery(getGrade, null);
        cursor.moveToFirst();
        String word = cursor.getString(cursor.getColumnIndex(Word.KEY_word));
        cursor.close();
        db.close();
        return word;
    }

    /**
     * 通过wordid来获取单词的grade和出现次数
     * @param wordid 单词ID
     * @return 单词的用户信息
     */
    public WordU getWordU(int wordid){
        db = dbHelper.getWritableDatabase();
        String getGrade = "SELECT *" +
                " FROM " + TABLENAME +
                " WHERE " + WordU.KEY_ID + "=" + wordid;
        cursor = db.rawQuery(getGrade, null);
        cursor.moveToFirst();
        WordU wordu = new WordU(wordid,
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(WordU.KEY_grade))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(WordU.KEY_occurtime))));
        cursor.close();
        db.close();
        return wordu;
    }

    /**
     * 获取当前单词本的单词总数
     * @return 单词总数
     */
    public int getTotal(){
        int num = 0;
        db = dbHelper.getReadableDatabase();
        String sortByGrade = "SELECT count(*)" +
                " FROM " +
                BOOKNAME ;
        cursor = db.rawQuery(sortByGrade, null);
        if(cursor.moveToFirst()){
            num = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return num;
    }

    /**
     * 获取已出现过的单词数
     * @return 已出现过的单词数
     */
    public int getLearned(){
        int num = 0;
        db = dbHelper.getReadableDatabase();
        String sortByGrade = "SELECT count(*)" +
                " FROM " +
                TABLENAME +
                " WHERE " + WordU.KEY_occurtime+" > 0";
        cursor = db.rawQuery(sortByGrade, null);
        if(cursor.moveToFirst()){
            num = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return num;
    }
}
