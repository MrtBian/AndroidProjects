package com.nju.wing.accounting;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wing on 2017/8/5.
 */

public class SmsManger {
    private Context context;
    private static final long DayTimeMillis = 24 * 60 * 60 * 1000;
    private int days = 10;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    SmsManger(Context con){
        context = con;
    }
    public Map<String,String> getSmsFromPhone() {
        Map<String,String> mess = new HashMap<>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id,address,person,date,body,type" };//"_id", "address", "person",, "date", "type
        String where = " address = '95588' AND date >  "
                + (System.currentTimeMillis() - days * DayTimeMillis);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (cur.moveToFirst()) {
            do {
                String number = cur.getString(cur.getColumnIndex("address"));//手机号
                String date = cur.getString(cur.getColumnIndex("date"));//日期
                String body = cur.getString(cur.getColumnIndex("body"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(new Date(Long.parseLong(date)));
                mess.put(dateString,body);
                //System.out.println(number +" "+dateString+" "+body);
            }while(cur.moveToNext());
        }
        return mess;
    }
}
