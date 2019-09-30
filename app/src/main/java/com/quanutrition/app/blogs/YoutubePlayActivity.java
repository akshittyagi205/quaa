package com.quanutrition.app.blogs;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.quanutrition.app.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;


public class YoutubePlayActivity extends AppCompatActivity {

    YouTubePlayerView youTubePlayerView;
    String video="";
    Float sec=0.0f;
    YouTubePlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_youtube_play);
        youTubePlayerView = findViewById(R.id.player);
        video=getIntent().getExtras().getString("video");
//        sec = getIntent().getExtras().getFloat("sec");
        Log.d("Seconds",sec+"");
        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                player = initializedYouTubePlayer;
                player.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo(video, 0);
//                        initializedYouTubePlayer.seekTo(sec);

                    }
                });
            }
        }, true);
        youTubePlayerView.toggleFullScreen();
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {

            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
//        youTubePlayerView.release();
        player.pause();
        super.onPause();
    }
}
