package com.example.mayank.abcd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by mayanktripathi on 24/12/16.
 */

public class TakeTestActivity extends AppCompatActivity {

    Button speaking , listening;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_test);

        speaking = (Button)findViewById(R.id.speaking);
        listening = (Button)findViewById(R.id.listening);

        speaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakeTestActivity.this , ListenerActivity.class);
                startActivity(intent);
            }
        });
    }





}
