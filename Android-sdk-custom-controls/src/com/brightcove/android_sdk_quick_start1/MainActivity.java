package com.brightcove.android_sdk_quick_start1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.media.Catalog;
import com.brightcove.player.media.VideoListener;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveVideoView;

public class MainActivity extends Activity {
  public static final String TAG = "**VIDEO INFO**";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final BrightcoveVideoView bcVideoView = (BrightcoveVideoView) findViewById(R.id.bc_video_view);
    final Catalog catalog = new Catalog("WDGO_XdKqXVJRVGtrNuGLxCYDNoR-SvA5yUqX2eE6KjgefOxRzQilw..");
    final Button playButton = (Button) findViewById(R.id.play_button);
    final Button pauseButton = (Button) findViewById(R.id.pause_button);
    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    final EventEmitter eventEmitter = bcVideoView.getEventEmitter();
    
    //Both buttons initially disabled, 
    playButton.setEnabled(false);
    pauseButton.setEnabled(false);

    playButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        bcVideoView.start();
      }
    });

    pauseButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        bcVideoView.pause();
      }
    });

    //On DID_SET_SOURCE enable the play button
    eventEmitter.on(EventType.DID_SET_SOURCE, new EventListener() {
      
      @Override
      public void processEvent(Event arg0) {
        playButton.setEnabled(true);
      }
    });
    
    eventEmitter.on(EventType.VIDEO_DURATION_CHANGED, new EventListener() {

      @Override
      public void processEvent(Event event) {
        progressBar.setMax(event.getIntegerProperty(Video.Fields.DURATION));
      }
    });

    eventEmitter.on(EventType.DID_PLAY, new EventListener() {

      @Override
      public void processEvent(Event event) {
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
      }
    });

    eventEmitter.on(EventType.DID_PAUSE, new EventListener() {

      @Override
      public void processEvent(Event event) {
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
      }
    });

    eventEmitter.on(EventType.PROGRESS, new EventListener() {

      @Override
      public void processEvent(Event event) {
        progressBar.setProgress(event.getIntegerProperty(Event.PLAYHEAD_POSITION));
      }
    });

    catalog.findVideoByID("928199562001", new VideoListener() {

      @Override
      public void onError(String error) {
        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
        toast.show();
      }

      @Override
      public void onVideo(Video video) {
        bcVideoView.add(video);
        bcVideoView.start();
      }
    });
  }
}
