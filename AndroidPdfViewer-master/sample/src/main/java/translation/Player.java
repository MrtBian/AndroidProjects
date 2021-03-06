package translation;

/**
 * Created by Wing on 2017/9/1.
 */


import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;

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


    public boolean playUrl (String videoUrl) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(videoUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        return true;
    }

    public void pause () {
        mediaPlayer.pause();
    }

    public void stop () {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



}
