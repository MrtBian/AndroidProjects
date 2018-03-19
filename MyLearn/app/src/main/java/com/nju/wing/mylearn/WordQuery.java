package com.nju.wing.mylearn;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Wing on 2017/7/21.
 */

public class WordQuery {
    //表名
    public static final String TABLE="CET6";
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public Cursor cursor;
    public WordQuery(Context context){
        dbHelper = new DBHelper(context);
        dbHelper.openDatabase();
    }
    protected void finalize() throws java.lang.Throwable {
        dbHelper.close();
        // 递归调用超类中的finalize方法
        super.finalize();
    }
    public void queryByDiff(){
        db = dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Word.KEY_ID+","+
                Word.KEY_word+","+
                Word.KEY_explain+","+
                Word.KEY_ping+","+
                Word.KEY_difficult+" FROM "+
                TABLE +
                " ORDER BY " +
                Word.KEY_difficult+";";
        cursor=db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
    }
    public void endQuery(){
        cursor.close();
        db.close();
    }
    public Word getNextWord(){
        Word nextWord = new Word();
        nextWord.wordID=Integer.parseInt(cursor.getString(cursor.getColumnIndex(Word.KEY_ID)));
        nextWord.word=cursor.getString(cursor.getColumnIndex(Word.KEY_word));
        nextWord.explain=cursor.getString(cursor.getColumnIndex(Word.KEY_explain));
        nextWord.ping=cursor.getString(cursor.getColumnIndex(Word.KEY_ping));
        nextWord.difficult = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Word.KEY_difficult)));
        cursor.moveToNext();
        //System.out.println(nextWord.word);
        return nextWord;
    }


}
