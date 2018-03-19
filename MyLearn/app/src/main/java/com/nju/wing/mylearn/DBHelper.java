package com.nju.wing.mylearn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.database.SQLException;

/**
 * Created by Wing on 2017/7/21.
 */

public class DBHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static final int DATABASE_VERSION=4;

    //数据库名称
    private static String DB_PATH = "/data/data/com.nju.wing.mylearn/databases/";
    private static String DB_NAME = "words.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DBHelper(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);
        this.myContext = context;
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DB_PATH + DB_NAME);
    }
    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();

    }
    public void openDatabase(){
        String databaseFile=DB_PATH + DB_NAME;
        //创建databases目录（不存在时）
        File file=new File(DB_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        //判断数据库是否存在
        if (new File(databaseFile).exists()) {
           /* File datafile = new File(databaseFile);
            datafile.delete();*/
        }
        else {
            //把数据库拷贝到/data/data/<package_name>/databases目录下
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(databaseFile);
                //数据库放assets目录下
                InputStream inputStream = myContext.getAssets().open(DB_NAME);
                //数据库方res/rew目录下
                byte[] buffer = new byte[1024];
                int readBytes = 0;

                while ((readBytes = inputStream.read(buffer)) != -1)
                    fileOutputStream.write(buffer, 0, readBytes);

                    inputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
            }
        }

        myDataBase = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

/*    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }*/
}
