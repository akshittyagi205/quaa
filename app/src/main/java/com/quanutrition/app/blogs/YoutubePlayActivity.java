package com.quanutrition.app.blogs;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.quanutrition.app.R;


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
        youTubePlayerView.initialize(new YouTubePlayerListener() {
            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String videoId) {
                // Called when the video ID is loaded
                Log.d("YouTubePlayer", "Video ID: " + videoId);
            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float loadedFraction) {
                // Called when the video buffer is loaded partially (value between 0.0 and 1.0)
                Log.d("YouTubePlayer", "Video Loaded: " + (loadedFraction * 100) + "%");
            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float duration) {
                // Called when the total duration of the video is known (in seconds)
                Log.d("YouTubePlayer", "Video Duration: " + duration + " seconds");
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float currentSecond) {
                // Called periodically as the video plays to update the current playback time
                Log.d("YouTubePlayer", "Current Second: " + currentSecond);
            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {
                // Called when an error occurs
                Log.e("YouTubePlayer", "Error: " + playerError);
            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
                // Called when the playback speed changes
                Log.d("YouTubePlayer", "Playback Rate: " + playbackRate);
            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {
                // Called when the playback quality changes (e.g., 720p, 1080p, etc.)
                Log.d("YouTubePlayer", "Playback Quality: " + playbackQuality);
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                // Called when the player's state changes (e.g., playing, paused, buffering)
                Log.d("YouTubePlayer", "Player State: " + playerState);
                if (playerState == PlayerConstants.PlayerState.ENDED) {
                    // Handle the end of the video
                    Log.d("YouTubePlayer", "Video ended");
                }
            }

            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(video, 0f);
            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {
                // Called when the YouTube API changes, though this is rare
                Log.d("YouTubePlayer", "API has changed");
            }
        }, true);
    }

    @Override
    protected void onPause() {
//        youTubePlayerView.release();
        player.pause();
        super.onPause();
    }
}
