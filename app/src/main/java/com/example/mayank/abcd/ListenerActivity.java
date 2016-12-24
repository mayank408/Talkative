package com.example.mayank.abcd;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mayank on 03-12-2016.
 */

public class ListenerActivity extends AppCompatActivity {

    private static final String URL_COUNT = "http://sakshi.pythonanywhere.com/words_count";


    @BindView(R.id.btnSpeak)
    ImageButton btnSpeak;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    public String speechtotext;
    TextView textstring, speechOut;
    ProgressDialog pd;
    Button count;


    String str[] = {"It will take me about five minutes to home",
            "Where is my food",
            "I love coding as everyone does",
            "Android is an operating system",
            "Hard work is the key to success"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listener);
        ButterKnife.bind(this);


        pd=new ProgressDialog(ListenerActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loggin In....");


        textstring = (TextView) findViewById(R.id.text);
        speechOut = (TextView) findViewById(R.id.speechout);
        count = (Button) findViewById(R.id.button);

        textstring.setText(str[randInt(0, 4)]);
        speechOut.setText(speechtotext);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordcount();
            }
        });

    }

    public static int randInt(int min, int max) {

        Random ran = new Random();
        int x = ran.nextInt(max - min + 1) + min;

        return x;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, (getString(R.string.speech_not_supported)), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler mHandler = new Handler(Looper.getMainLooper());
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        speechtotext = result.get(0);


                        if(speechtotext!=null)
                        {
                            String strim = str[randInt(0,4)].trim();
                            if(speechtotext.trim().equalsIgnoreCase(strim))
                            {
                                speechOut.setText("Correct !!!");
                            }
                            else
                            {
                                speechOut.setText("Wrong !!!");
                            }
                        }
                    }
                });
            }

        }


    }


    private void wordcount() {
        pd.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.hide();

                        Toast.makeText(ListenerActivity.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject json = new JSONObject(response);
                            String str = json.getString("status");
                            Toast.makeText(ListenerActivity.this, str, Toast.LENGTH_LONG).show();
                            //if(str.equals("success"))
                            {
                                Intent intent = new Intent(ListenerActivity.this, ListenerActivity.class);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListenerActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer", "how are you kha ja rhe ho");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




}


