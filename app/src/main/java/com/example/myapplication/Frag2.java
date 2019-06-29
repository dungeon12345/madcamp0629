package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;


public class Frag2 extends Fragment implements View.OnClickListener {

    //private Toolbar toolbar;
    private Toolbar toolbar;
    private int RECORD_AUDIO_REQUEST_CODE =123 ;
    private ImageView imageViewRecord, imageViewPlay, imageViewStop;
    private LinearLayout linearLayoutRecorder, linearLayoutPlay;
    private SeekBar seekBar;
    private Chronometer chronometer;
    private boolean isPlaying = false;
    private String fileName = null;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    int [] ImageId = { R.drawable.ic_microphone, R.drawable.ic_stop, R.drawable.ic_pause, R.drawable.ic_play };
    private View v;
    public Frag2() {
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    private void initViews() {
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Voice Recorder");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.list_menu);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.item_list)
                {
                    // do something
                    gotoRecodingListActivity();
                }
                else{
                    // do something
                }

                return false;
            }
        });

        linearLayoutRecorder =  v.findViewById(R.id.linearLayoutRecorder);
        chronometer = (Chronometer) v.findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        imageViewRecord =  v.findViewById(R.id.imageViewRecord);
        imageViewStop =  v.findViewById(R.id.imageViewStop);
        imageViewPlay =  v.findViewById(R.id.imageViewPlay);
        linearLayoutPlay = v.findViewById(R.id.linearLayoutPlay);
        seekBar =  v.findViewById(R.id.seekBar);

        imageViewRecord.setOnClickListener(new MyListener());
        imageViewStop.setOnClickListener(new MyListener());
        imageViewPlay.setOnClickListener(new MyListener());

    }

    @Override
    public void onClick(View view) {

    }

    class MyListener implements View.OnClickListener {

        int i = 0;
        int length = ImageId.length;
        final TextView tv = (TextView)v.findViewById(R.id.section_label);

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            if( v == imageViewRecord ){
                prepareforRecording();
                startRecording();
            }else if( v == imageViewStop ){
                prepareforStop();
                stopRecording();
            }else if( v == imageViewPlay ){
                if( !isPlaying && fileName!=null){
                    isPlaying = true;
                    startPlaying();
                }else{
                    isPlaying = false;
                    stopPlaying();
                }
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_frag2, container, false);

        initViews() ;

        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);up
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        return ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_list:
                gotoRecodingListActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void gotoRecodingListActivity() {
        Intent intent = new Intent(getContext(), RecordingListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    //@Override
//    public void onClick(View view) {
//
//        if( view == imageViewRecord ){
//            prepareforRecording();
//            startRecording();
//        }else if( view == imageViewStop ){
//            prepareforStop();
//            stopRecording();
//        }else if( view == imageViewPlay ){
//            if( !isPlaying && fileName != null ){
//                isPlaying = true;
//                startPlaying();
//            }else{
//                isPlaying = false;
//                stopPlaying();
//            }
//        }

//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void prepareforStop() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        linearLayoutPlay.setVisibility(View.VISIBLE);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void prepareforRecording() {
        TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        linearLayoutPlay.setVisibility(View.GONE);
    }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        stopPlaying();
        // making the imageview a stop button
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
        Toast.makeText(getContext(), "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }


    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometer.stop();

    }



    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);  //pause 버튼을 보여줌

        seekBar.setProgress(lastProgress); // seekBar을 설정
        mPlayer.seekTo(lastProgress);  // mPlayer 설정
        seekBar.setMax(mPlayer.getDuration()); // seekBar의 max를 설정
        seekUpdation();

        if(lastProgress != 0 ){                               //재생이 완료되지 않은 상태에서 정지 버튼을 누른 경우
            mPlayer.seekTo(lastProgress);
            chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
        }

        chronometer.start();


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {     //노래가 끝나면 chronometer 끝 O
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometer.stop();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {      // SEEK BAR를 바꾸면 사간이 바뀜
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {         //
                if( mPlayer!=null && fromUser ){
                    mPlayer.seekTo(progress);
                    chronometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;     // lastprogress 에 progress 저장, 이는 나중에 startplaying에서 기준점을 잡는데 사용됨.
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        }
    }

    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                getActivity().finishAffinity();
            }
        }

    }
}