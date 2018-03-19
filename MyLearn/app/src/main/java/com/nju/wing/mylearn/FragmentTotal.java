package com.nju.wing.mylearn;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wing on 2017/7/23.
 */

public class FragmentTotal extends Fragment implements Spinner.OnItemSelectedListener{

    private String tableName;
    private String userName = "Wing";
    private ListView listView;

    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_total, container, false);
        tableName = ((MainActivity)getActivity()).getTableName();
        listView = (ListView)view.findViewById(R.id.list_word);
        UserWord uword = new UserWord(getActivity(),tableName,userName);
        WordsAsyncTask wordsAsyncTask = new WordsAsyncTask();
        wordsAsyncTask.onPostExecute(wordsAsyncTask.doInBackground(uword));
        /*List<ItemWord> itemList = new ArrayList<>();
        List<Integer> wordList = uword.getSortByGrade();
        for(int id:wordList){
            WordU wu = uword.getWordU(id);
            String word = uword.getWord(id);
            itemList.add(new ItemWord(word,wu.grade,wu.occurtime));
        }

        listView.setAdapter(new MyBaseAdapter(getActivity(), itemList));*/
        TextView subject = (TextView)view.findViewById(R.id.text_subject);
        TextView total = (TextView)view.findViewById(R.id.text_totalnum);
        TextView num = (TextView)view.findViewById(R.id.text_wordnum);
        /*subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                UserWord uword = new UserWord(getActivity(),tableName,userName);
                WordsAsyncTask wordsAsyncTask = new WordsAsyncTask();
                wordsAsyncTask.onPostExecute(wordsAsyncTask.doInBackground(uword));
                TextView subject = (TextView)getActivity().findViewById(R.id.text_subject);
                TextView total = (TextView)getActivity().findViewById(R.id.text_totalnum);
                TextView num = (TextView)getActivity().findViewById(R.id.text_wordnum);
                subject.setText(tableName);
                total.setText(uword.getTotal()+"");
                num.setText(uword.getLearned()+"");
            }
        });*/
        subject.setText(tableName);
        total.setText(uword.getTotal()+"");
        num.setText(uword.getLearned()+"");

        spinner = (Spinner)view.findViewById(R.id.sp_subject);

        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String[] languages = getResources().getStringArray(R.array.books);
        tableName = languages[pos];
        ((MainActivity)getActivity()).setTableName(tableName);
        dataflash();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * 更新统计界面的信息
     */
    public void dataflash(){
        UserWord uword = new UserWord(getActivity(),tableName,userName);
        //uword.setCurrentSubject(tableName);
        TextView subject = (TextView)getActivity().findViewById(R.id.text_subject);
        TextView total = (TextView)getActivity().findViewById(R.id.text_totalnum);
        TextView num = (TextView)getActivity().findViewById(R.id.text_wordnum);
        subject.setText(tableName);
        total.setText(uword.getTotal()+"");
        num.setText(uword.getLearned()+"");

        WordsAsyncTask wordsAsyncTask = new WordsAsyncTask();
        wordsAsyncTask.onPostExecute(wordsAsyncTask.doInBackground(uword));
    }

    class WordsAsyncTask extends AsyncTask<UserWord,Void,List<ItemWord>>{

        @Override
        protected List<ItemWord> doInBackground (UserWord... userWords) {
            UserWord uword = userWords[0];
            List<ItemWord> itemList = new ArrayList<>();
            List<Integer> wordList = uword.getSortByGrade();
            for(int id:wordList){
                WordU wu = uword.getWordU(id);
                String word = uword.getWord(id);
                itemList.add(new ItemWord(word,wu.grade,wu.occurtime));
            }
            return itemList;
        }

        @Override
        protected void onPostExecute(List<ItemWord> itemWords){
            super.onPostExecute(itemWords);
            MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getActivity(),itemWords);
            listView.setAdapter(myBaseAdapter);
        }
    }
}
