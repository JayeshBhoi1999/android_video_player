package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class playVideo extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    LinearLayout linearLayout ;
    SurfaceView surfaceView;
    TextView textView;
    SeekBar seekBar;
    ArrayList videos;
    MediaPlayer mediaPlayer;
    String textcontent;
    ImageView play;
    Thread updateseek;
    int fade=0;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_play_video);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        linearLayout=findViewById(R.id.linearLayout);
        surfaceView=findViewById(R.id.surfaceView);
        textView=findViewById(R.id.textView);
        textView.setTextColor(Color.WHITE);
        seekBar=findViewById(R.id.seekBar);
        play=findViewById(R.id.play);

        linearLayout.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        videos=(ArrayList) bundle.getParcelableArrayList("videolist");
        textcontent=intent.getStringExtra("currentvideo");
        position=intent.getIntExtra("position",0);
        textView.setText(textcontent);
        Uri uri =Uri.parse(videos.get(position).toString());

        mediaPlayer=mediaPlayer.create(this,uri);
        surfaceView.setKeepScreenOn(true);
        SurfaceHolder surfaceHolder =surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               // if(b)
             //   {
               //     mediaPlayer.seekTo(i);
              //  }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });






        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fade==0){
                linearLayout.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                fade=1;
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    fade=0;
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()==true)
                {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.pause);
                }
                else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.play);
                }
            }
        });

        updateseek=new Thread(){
            @Override
            public void run() {
               int currentposition=0;
               try {
                   while(currentposition < mediaPlayer.getDuration())
                   {
                     currentposition =mediaPlayer.getCurrentPosition();
                     seekBar.setProgress(currentposition);
                     sleep(800);

                   }
               }catch(Exception e){e.printStackTrace();}


            }
        };
        updateseek.start();



    }
}