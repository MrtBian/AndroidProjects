package com.nju.wing.mylearn;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Wing on 2017/7/23.
 */

public class FragmentStudy extends Fragment {
    //WordQuery wordQuery;
    private static final int FORGET = 1;
    private static final int REMEMBER = 0;
    private static final int FAMILIAR = 2;

    UserWord userWord;
    Word word;      //当前界面显示的单词
    private Button buttonForget;
    private Button buttonRemember;
    private Button buttonFamiliar;
    private String tableName;
    private String userName = "Wing";
    private TextView textword;
    private TextView textexplain;
    private TextView textsentence;
    private TextView textpsymboluk;
    private TextView textpsymbolus;
    private TextView textsen;
    private ScrollView sview_sen;
    private MediaPlayer mp_uk;
    private MediaPlayer mp_us;
    private String url_uk;
    private String url_us;
    private Player myPlayer;
    private Player PlayerUK;
    private Player PlayerUS;

    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            WordInfo w = (WordInfo) msg.obj;
            textexplain.setText(w.getExplanation());
            textpsymboluk.setText(w.getPsymbol_uk());
            textpsymbolus.setText(w.getPsymbol_us());
            textsentence.setText(w.getSentences());
            url_uk = w.getPron_uk();
            url_us = w.getPron_us();
        }
    };
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_learn, container, false);
        tableName = ((MainActivity) getActivity()).getTableName();
        userWord = new UserWord(getActivity(), tableName, userName);
        word = userWord.getNextWord();

        textword = (TextView) view.findViewById(R.id.text_word);
        textexplain = (TextView) view.findViewById(R.id.text_explain);
        textsentence = (TextView) view.findViewById(R.id.text_sentence);
        textpsymboluk = (TextView) view.findViewById(R.id.psymbol_uk);
        textpsymbolus = (TextView) view.findViewById(R.id.psymbol_us);
        textsen = (TextView) view.findViewById(R.id.textView);
        sview_sen = view.findViewById(R.id.sview_sen);
        textword.setText(word.word);
        //textping.setText("["+word.ping+"]");
        //String str = StringTools.divStr(word.explain);
        //textexplain.setText(str);
        WordInfo w = new WordInfo(word.word);
        HandlerThread handlerThread = new HandlerThread("getWordInfo");
        handlerThread.start();
        Handler childHandler = new Handler(handlerThread.getLooper(),new ChildCallback());
        Message msg = new Message();
        msg.obj = w;
        childHandler.sendMessage(msg);
        //w.getWordInfo();
        myPlayer = new Player();
        sview_sen.fullScroll(View.FOCUS_UP);
        /*textexplain.setText(w.getExplanation());
        textpsymboluk.setText(w.getPsymbol_uk());
        textpsymbolus.setText(w.getPsymbol_us());
        textsentence.setText(w.getSentences());
        url_uk = w.getPron_uk();
        url_us = w.getPron_us();*/
        /*soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setMaxStreams(2);
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适的属性
        soundPoolBuilder.setAudioAttributes(attrBuilder.build());
        soundPool = soundPoolBuilder.build();
        soundPool.load(url_uk,1);
        soundPool.load(url_us,1);
        soundPool.play(1,1, 1, 0, 0, 1);*/
        /*mp_uk = new MediaPlayer();
        mp_us = new MediaPlayer();
        try {
            System.out.println(url_uk);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            mp_uk.setAudioAttributes(attrBuilder.build());
            mp_uk.setDataSource(url_uk);
            mp_us.setDataSource(url_us);
            mp_uk.prepareAsync();
            mp_us.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp_uk.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared (MediaPlayer mediaPlayer) {
                mp_uk.start();
            }
        });*/
        textexplain.setVisibility(View.INVISIBLE);
        textsen.setVisibility(View.INVISIBLE);
        textsentence.setVisibility(View.INVISIBLE);

        textpsymboluk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                myPlayer.playUrl(url_uk);
            }
        });
        textpsymbolus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                myPlayer.playUrl(url_us);
            }
        });
        buttonRemember = (Button) view.findViewById(R.id.button_remember);
        buttonRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                int wordID = word.wordID;
                userWord.updateGrade(REMEMBER, wordID);
                word = userWord.getNextWord();/*
                TextView textword = (TextView) getActivity().findViewById(R.id.text_word);
                //TextView textping = (TextView)getActivity().findViewById(R.id.text_ping);
                TextView textexplain = (TextView) getActivity().findViewById(R.id.text_explain);
                TextView textsentence = (TextView) getActivity().findViewById(R.id.text_sentence);
                TextView textsen = (TextView) getActivity().findViewById(R.id.textView);
                TextView textpsymboluk = (TextView) getActivity().findViewById(R.id.psymbol_uk);
                TextView textpsymbolus = (TextView) getActivity().findViewById(R.id.psymbol_us);
                */
                //sview_sen = getActivity().findViewById(R.id.sview_sen);
                sview_sen.fullScroll(View.FOCUS_UP);
                textword.setText(word.word);
                WordInfo w = new WordInfo(word.word);
                HandlerThread handlerThread = new HandlerThread("getWordInfo");
                handlerThread.start();
                Handler childHandler = new Handler(handlerThread.getLooper(),new ChildCallback());
                Message msg = new Message();
                msg.obj = w;
                childHandler.sendMessage(msg);
                //w.getWordInfo();
                myPlayer.release();
                myPlayer = new Player();
                if (textexplain.getVisibility() == View.VISIBLE) {
                    //textping.setVisibility(View.INVISIBLE);
                    textexplain.setVisibility(View.INVISIBLE);
                    textsen.setVisibility(View.INVISIBLE);
                    textsentence.setVisibility(View.INVISIBLE);
                }
            }
        });
        buttonForget = (Button) view.findViewById(R.id.button_forget);
        buttonForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                int wordID = word.wordID;
                userWord.updateGrade(FORGET, wordID);
                word = userWord.getNextWord();/*
                TextView textword = (TextView) getActivity().findViewById(R.id.text_word);
                //TextView textping = (TextView)getActivity().findViewById(R.id.text_ping);
                TextView textexplain = (TextView) getActivity().findViewById(R.id.text_explain);
                TextView textsentence = (TextView) getActivity().findViewById(R.id.text_sentence);
                TextView textsen = (TextView) getActivity().findViewById(R.id.textView);
                TextView textpsymboluk = (TextView) getActivity().findViewById(R.id.psymbol_uk);
                TextView textpsymbolus = (TextView) getActivity().findViewById(R.id.psymbol_us);*/
                //sview_sen = getActivity().findViewById(R.id.sview_sen);
                sview_sen.fullScroll(View.FOCUS_UP);
                textword.setText(word.word);
                WordInfo w = new WordInfo(word.word);
                HandlerThread handlerThread = new HandlerThread("getWordInfo");
                handlerThread.start();
                Handler childHandler = new Handler(handlerThread.getLooper(),new ChildCallback());
                Message msg = new Message();
                msg.obj = w;
                childHandler.sendMessage(msg);
                //w.getWordInfo();
                myPlayer.release();
                myPlayer = new Player();
                if (textexplain.getVisibility() == View.VISIBLE) {
                    //textping.setVisibility(View.INVISIBLE);
                    textexplain.setVisibility(View.INVISIBLE);
                    textsen.setVisibility(View.INVISIBLE);
                    textsentence.setVisibility(View.INVISIBLE);
                }
            }
        });
        buttonFamiliar = (Button) view.findViewById(R.id.button_familiar);
        buttonFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                int wordID = word.wordID;
                userWord.updateGrade(FAMILIAR, wordID);
                word = userWord.getNextWord();/*
                TextView textword = (TextView) getActivity().findViewById(R.id.text_word);
                //TextView textping = (TextView)getActivity().findViewById(R.id.text_ping);
                TextView textexplain = (TextView) getActivity().findViewById(R.id.text_explain);
                TextView textsentence = (TextView) getActivity().findViewById(R.id.text_sentence);
                TextView textsen = (TextView) getActivity().findViewById(R.id.textView);
                TextView textpsymboluk = (TextView) getActivity().findViewById(R.id.psymbol_uk);
                TextView textpsymbolus = (TextView) getActivity().findViewById(R.id.psymbol_us);*/
                //sview_sen = view.findViewById(R.id.sview_sen);
                sview_sen.fullScroll(View.FOCUS_UP);
                textword.setText(word.word);
                WordInfo w = new WordInfo(word.word);
                HandlerThread handlerThread = new HandlerThread("getWordInfo");
                handlerThread.start();
                Handler childHandler = new Handler(handlerThread.getLooper(),new ChildCallback());
                Message msg = new Message();
                msg.obj = w;
                childHandler.sendMessage(msg);
                //w.getWordInfo();
                myPlayer.release();
                myPlayer = new Player();
                if (textexplain.getVisibility() == View.VISIBLE) {
                    //textping.setVisibility(View.INVISIBLE);
                    textexplain.setVisibility(View.INVISIBLE);
                    textsen.setVisibility(View.INVISIBLE);
                    textsentence.setVisibility(View.INVISIBLE);
                }
            }
        });

        sview_sen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //System.out.println("ACTION_UP");
                        /*TextView textexplain = (TextView) getActivity().findViewById(R.id.text_explain);
                        TextView textsentence = (TextView) getActivity().findViewById(R.id.text_sentence);
                        TextView textsen = (TextView) getActivity().findViewById(R.id.textView);*/
                        //textping.setVisibility(View.VISIBLE);
                        textexplain.setVisibility(View.VISIBLE);
                        textsen.setVisibility(View.VISIBLE);
                        textsentence.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        return view;
    }
    /**
     * 该callback运行于子线程
     */
    class ChildCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            //在子线程中进行网络请求
            WordInfo w = (WordInfo)msg.obj;
            w.getWordInfo();
            Message msg1 = new Message();
            msg1.obj=w;

            //通知主线程去更新UI
            mUIHandler.sendMessage(msg1);
            return false;
        }
    }
    public void changeBook () {
        tableName = ((MainActivity) getActivity()).getTableName();
        userWord = new UserWord(getActivity(), tableName, userName);
        word = userWord.getNextWord();
        /*TextView textword = (TextView) getActivity().findViewById(R.id.text_word);
        TextView textexplain = (TextView) getActivity().findViewById(R.id.text_explain);
        TextView textsentence = (TextView) getActivity().findViewById(R.id.text_sentence);
        TextView textpsymboluk = (TextView) getActivity().findViewById(R.id.psymbol_uk);
        TextView textpsymbolus = (TextView) getActivity().findViewById(R.id.psymbol_us);
        TextView textsen = (TextView) getActivity().findViewById(R.id.textView);*/
        textword.setText(word.word);
        WordInfo w = new WordInfo(word.word);
        HandlerThread handlerThread = new HandlerThread("getWordInfo");
        handlerThread.start();
        Handler childHandler = new Handler(handlerThread.getLooper(),new ChildCallback());
        Message msg = new Message();
        msg.obj = w;
        childHandler.sendMessage(msg);
        myPlayer.release();
        myPlayer = new Player();
        textexplain.setVisibility(View.INVISIBLE);
        textsen.setVisibility(View.INVISIBLE);
        textsentence.setVisibility(View.INVISIBLE);
    }

}