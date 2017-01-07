package com.example.mayank.abcd;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;



/**
 * Created by mayanktripathi on 24/12/16.
 */


public class AudioActivity extends ActionBarActivity {
    private TextView recordTimeText, timekeeper;
    private ImageButton audioSendButton;
    private Button play;
    private View recordPanel;
    File fileName;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    MediaPlayer mediaPlayer ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    private static final String TAG = "AudioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);
        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);
        audioSendButton = (ImageButton) findViewById(R.id.chat_audio_send_button);
        TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
        timekeeper = (TextView)findViewById(R.id.textView4);
        play = (Button)findViewById(R.id.button2);
        textView.setText("SlideToCancel");
        random = new Random();

        audioSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = dp(30);
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;
                    // startRecording();
                    startrecord();
                    audioSendButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startedDraggingX = -1;
                    stoprecord();
                    // stopRecording(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    if (x < -distCanMove) {
                        stoprecord();
                        // stopRecording(false);
                    }
                    x = x + ViewProxy.getX(audioSendButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        params.leftMargin = dp(30) + (int) dist;
                        slideText.setLayoutParams(params);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }
                        ViewProxy.setAlpha(slideText, alpha);
                    }
                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                            + dp(30)) {
                        if (startedDraggingX == -1) {
                            startedDraggingX = x;
                            distCanMove = (recordPanel.getMeasuredWidth()
                                    - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                            if (distCanMove <= 0) {
                                distCanMove = dp(80);
                            } else if (distCanMove > dp(80)) {
                                distCanMove = dp(80);
                            }
                        }
                    }
                    if (params.leftMargin > dp(30)) {
                        params.leftMargin = dp(30);
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer = new MediaPlayer();
                try {if(AudioSavePathInDevice!=null)
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(AudioActivity.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

    }





    private void startrecord() {
        // TODO Auto-generated method stub
        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            CreateRandomAudioFileName(5) + "AudioRecording.3gp";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Toast.makeText(AudioActivity.this, "Recording started",
                    Toast.LENGTH_LONG).show();


    }

    private void MediaRecorderReady() {

        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }


    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void stoprecord() {



        // TODO Auto-generated method stub

        try{
            mediaRecorder.stop();
            doFileUpload();
            encodeAudio(AudioSavePathInDevice);


        }catch(RuntimeException stopException){
            //handle cleanup here
        }


        Toast.makeText(AudioActivity.this, "Recording Completed",
                Toast.LENGTH_LONG).show();

        if (timer != null) {
            timer.cancel();
        }

        timekeeper.setText(recordTimeText.getText().toString());

        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        recordTimeText.setText("00:00");
        vibrate();


        if(mediaPlayer!=null)
        { mediaPlayer.release();
        mediaPlayer.stop();}


    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }

    }
        private void doFileUpload(){



            byte[] videoBytes;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(new File(AudioSavePathInDevice));

                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = fis.read(buf)))
                    baos.write(buf, 0, n);

                 videoBytes = baos.toByteArray();


                String video_str = Base64.encodeToString(videoBytes , 0);
                Log.v( "hiiiii", "video array"+video_str);


            } catch (Exception e) {
                // TODO: handle exception
            }}


    private void encodeAudio(String selectedPath) {


        byte[] audioBytes;
        try {

            // Just to check file size.. Its is correct i-e; Not Zero
            File audioFile = new File(selectedPath);
            long fileSize = audioFile.length();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(selectedPath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            // Here goes the Base64 string
            String _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);

            Log.d("Audio In Bytes" , _audioBase64);



            decodeAudio(_audioBase64 , fileName , AudioSavePathInDevice  , mediaPlayer );

        } catch (Exception e) {

        }

    }

    private void decodeAudio(String base64AudioData, File fileName, String path, MediaPlayer mp) {

        Log.d(TAG, "decodeAudio: called ");

        try {

            Log.d(TAG, "decodeAudio: try first block");
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(Base64.decode(base64AudioData.getBytes(), Base64.DEFAULT));
            fos.close();

            try {

                Log.d(TAG, "decodeAudio: try second block");
                
                Toast.makeText(this, "hvfy", Toast.LENGTH_SHORT).show();
                Log.e("bhvcfgvh" , "uhuhu");

                mp = new MediaPlayer();
                mp.setDataSource(path);
                mp.prepare();
                mp.start();

            } catch (Exception e) {

                Log.e(TAG, "decodeAudio: exception");
                //DiagnosticHelper.writeException(e);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}