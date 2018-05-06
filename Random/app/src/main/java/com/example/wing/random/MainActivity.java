package com.example.wing.random;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the Roadom button */
    public void clickRandom(View view) {
        // Do something in response to button
        EditText minnum = (EditText)findViewById(R.id.minnum);
        EditText maxnum = (EditText)findViewById(R.id.maxnum);
        EditText num_ran = (EditText)findViewById(R.id.num_ran);
        int max = Integer.parseInt(maxnum.getText().toString());
        int min = Integer.parseInt(minnum.getText().toString());
        int num = Integer.parseInt(num_ran.getText().toString());
        int []randomarray = randomArray(min,max,num);
        listView = (ListView) findViewById(R.id.listview);
        list.add(ints2str(randomarray));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_layout,list);
        listView.setAdapter(arrayAdapter);
        //TextView textView = (TextView) findViewById(R.id.listview);
        //textView.setText(ints2str(randomarray));
        //textView.setText(chars,0,chars.length);
    }

    /** Called when the user clicks the Reset button */
    public void clickReset(View view) {
        // Do something in response to button
        EditText minnum = (EditText)findViewById(R.id.minnum);
        EditText maxnum = (EditText)findViewById(R.id.maxnum);
        EditText num_ran = (EditText)findViewById(R.id.num_ran);
        minnum.setText("");
        maxnum.setText("");
        num_ran.setText("");
        listView = (ListView) findViewById(R.id.listview);
        list.clear();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.my_layout,list);
        listView.setAdapter(arrayAdapter);
        //TextView textView = (TextView) findViewById(R.id.listview);
        //textView.setText("");
    }
    public String ints2str(int []arr){
        String str = "";
        for(int i=0;i<arr.length;i++){
            str=str + " "+arr[i];
        }
        return str;
    }
    /**
     * 生成随机数
     * @param min
     * @param max
     * @param n
     * @return
     */
    public int[] randomArray(int min,int max,int n){
        int len = max-min+1;

        if(max < min || n > len){
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        Arrays.sort(result);
        return result;
    }
}
