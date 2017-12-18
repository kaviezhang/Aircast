package com.example.administrator.aircast;

/**
 * Created by Administrator on 2017/8/28.
 */

import android.content.Context;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements OnBufferingUpdateListener,
        OnCompletionListener, MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback {

    private TextureView textureView;
    private SurfaceView surfaceView;
    private MainActivity mainactivity;



    private int videoWidth;
    private int videoHeight;
    public MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SeekBar skbProgress;
    private Timer mTimer=new Timer();
    public Player(SurfaceView surfaceView, SeekBar skbProgress)
    {
        this.skbProgress=skbProgress;
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(mediaPlayer==null)
                return;
            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);
            }
        };
    };
    //*****************************************************

    public void play()
    {
        mediaPlayer.start();
    }

    public void playUrl(String videoUrl)
    {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            //changeVideoSize();
            mediaPlayer.prepare();//prepare之后自动播放
            mediaPlayer.seekTo(0);
            mediaPlayer.setLooping(true);//设置视频循环播放
            Log.e("start","ready");
            //mediaPlayer.start();
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


    public void pause()
    {
        mediaPlayer.pause();
    }

    public void stop()
    {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.e("mediaPlayer", "surface changed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
        Log.e("mediaPlayer", "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.e("mediaPlayer", "surface destroyed");
    }


    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        videoWidth = mediaPlayer.getVideoWidth();
        videoHeight = mediaPlayer.getVideoHeight();
        if (videoHeight != 0 && videoWidth != 0) {
            arg0.start();
        }
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        skbProgress.setSecondaryProgress(bufferingProgress);
        int currentProgress=skbProgress.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
        Log.e(currentProgress+"% play", bufferingProgress + "% buffer");

    }


    public Context mContext;
    public float screenWidth;
    public float screenHeight;
    /**
     * 改变视频的显示大小，全屏，窗口，内容
     */
    public void changeVideoSize() {
        Log.e("changesize","sizesize");

        //DisplayMetrics metric = mainactivity.getContext().getResources().getDisplayMetrics();
        //System.out.println("heigth : " + metric.heightPixels);
        //System.out.println("width : " + metric.widthPixels);

        //获取屏幕的大小
        //DisplayMetrics metric = new DisplayMetrics();
        //((MainActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
        //mainactivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth=screenHeight=1000;
        //screenWidth = metric.widthPixels;
        //screenHeight = metric.heightPixels;
        System.out.println(screenHeight);
        System.out.println(screenWidth);
        // 获取视频的宽度和高度
        int mVideoWidth = mediaPlayer.getVideoWidth();
        int mVideoHeight = mediaPlayer.getVideoHeight();
        // 如果按钮文字为窗口则设置为窗口模式
            /*
             * 如果为全屏模式则改为适应内容的，前提是视频宽高小于屏幕宽高，如果大于宽高 我们要做缩放
             * 如果视频的宽高度有一方不满足我们就要进行缩放. 如果视频的大小都满足就直接设置并居中显示。
             */
//        if (width > screenWidth || height > screenHeight) {
//            // 计算出宽高的倍数
//            float vWidth = (float) width / (float) screenWidth;
//            float vHeight = (float) height / (float) screenHeight;
//            // 获取最大的倍数值，按大数值进行缩放
//            float max = Math.max(vWidth, vHeight);
//            // 计算出缩放大小,取接近的正值
//            width = (int) Math.ceil((float) width / max);
//            height = (int) Math.ceil((float) height / max);
//        }
//        ViewGroup.LayoutParams layoutParams =getLayoutParams();
//        layoutParams.width = videoWidth;
//        layoutParams.height = videoHeight;
//        setLayoutParams(layoutParams);


        float sx = (float) screenWidth / (float) mVideoWidth;
        float sy = (float) screenHeight / (float) mVideoHeight;

        Matrix matrix = new Matrix();

        //第1步:把视频区移动到View区,使两者中心点重合.
        matrix.preTranslate((screenWidth - mVideoWidth) / 2, (screenHeight - mVideoHeight) / 2);

        //第2步:因为默认视频是fitXY的形式显示的,所以首先要缩放还原回来.
        matrix.preScale(mVideoWidth / (float) screenWidth, mVideoHeight / (float) screenHeight);

        //第3步,等比例放大或缩小,直到视频区的一边和View一边相等.如果另一边和view的一边不相等，则留下空隙
        if (sx >= sy){
            matrix.postScale(sy, sy, screenWidth / 2, screenHeight / 2);
        }else{
            matrix.postScale(sx, sx, screenWidth / 2, screenHeight / 2);
        }

        //ViewGroup.LayoutParams layoutParams =getLayoutParams();
//        layoutParams.width = videoWidth;
//        layoutParams.height = videoHeight;
//        setLayoutParams(layoutParams);
        //textureView.setTransform(matrix);
        //surfaceView.postInvalidate();

    }




}
