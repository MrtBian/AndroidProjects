package com.nju.wing.mylearn;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rg_tab_bar;
    //private RadioButton rb_channel;
    private RadioButton rb_learn;
    private RadioButton rb_total;

    //Fragment Object
    private FragmentStudy fg1;
    private FragmentTotal fg2;
    private FragmentManager fManager;

    private String tableName = "CET6";
    private String userName = "Wing";

    public String getTableName(){
        return tableName;
    }
    public void setTableName(String name){
        tableName = name;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/
        //tableName = new UserWord(this,tableName,userName).getCurrentSubject();
        //System.out.println(tableName);
        fManager = getFragmentManager();
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);

        //获取第一个单选按钮，并设置其为选中状态
        rb_learn = (RadioButton) findViewById(R.id.rb_learn);
        rb_total = (RadioButton) findViewById(R.id.rb_total);
        rb_learn.setChecked(true);
        rb_learn.setBackgroundColor(this.getResources().getColor(R.color.lightgray));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId){
            case R.id.rb_learn:
                rb_learn.setBackgroundColor(this.getResources().getColor(R.color.lightgray));
                rb_total.setBackgroundColor(this.getResources().getColor(R.color.white));
                if(fg1 == null){
                    fg1 = new FragmentStudy();
                    fTransaction.add(R.id.ly_content,fg1);
                }else{
                    fg1.changeBook();
                    fTransaction.show(fg1);
                }
                break;
            case R.id.rb_total:
                rb_learn.setBackgroundColor(this.getResources().getColor(R.color.white));
                rb_total.setBackgroundColor(this.getResources().getColor(R.color.lightgray));
                if(fg2 == null){
                    fg2 = new FragmentTotal();
                    fTransaction.add(R.id.ly_content,fg2);
                }else{
                    fg2.dataflash();
                    fTransaction.show(fg2);
                }
                break;
        }
        fTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
    }
}
