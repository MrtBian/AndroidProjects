package com.nju.wing.mylearn;

/**
 * Created by Wing on 2017/9/1.
 */


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.SeekBar;

public class Player {
    public MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Player (String URL) {

        try {
            mediaPlayer = new MediaPlayer();
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setAudioAttributes(attrBuilder.build());
            mediaPlayer.setDataSource(URL);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

    }

    public void play () {
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void release(){
        mediaPlayer.release();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Player () {

        try {
            mediaPlayer = new MediaPlayer();
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setAudioAttributes(attrBuilder.build());
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

    }


    public void playUrl (String videoUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pause () {
        mediaPlayer.pause();
    }

    public void stop () {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



}
