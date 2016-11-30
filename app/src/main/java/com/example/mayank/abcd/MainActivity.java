package com.example.mayank.abcd;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextView username;
    @BindView(R.id.password_input)
    TextView password;

    Button login = (Button)findViewById(R.id.log_in);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    String Username = username.getText().toString().trim();
    String Password = password.getText().toString().trim();





}
