package com.example.basketbattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        videoView = findViewById(R.id.videoView);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
        videoView.setVideoURI(video);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                startNextActivity();
            }
        });

        videoView.start();
    }

    private void startNextActivity() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}