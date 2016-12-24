package com.example.mayank.abcd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayank on 02-12-2016.
 */

public class SignUp extends AppCompatActivity {

    private static final String URL_SIGNUP = "http://sakshi.pythonanywhere.com/signup";
    TextView username , password , compassword , contact , email ;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = (TextView) findViewById(R.id.username_signup);
        password = (TextView) findViewById(R.id.password_signup);
        contact = (TextView) findViewById(R.id.contact);
        compassword = (TextView) findViewById(R.id.confirm_password);
        email = (TextView) findViewById(R.id.emailsignup);
        Button SignUp = (Button) findViewById(R.id.sign_up);

        pd=new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Signing Up....");


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();



            }
        });

    }

    private void registerUser() {
        pd.show();
        final String Username = username.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String compass = compassword.getText().toString().trim();
        final String Contact = contact.getText().toString().trim();
        final String emailsignup = email.getText().toString().trim();

        if(!Password.equals(compass))
        {
            /*final AlertDialog.Builder error = new AlertDialog.Builder(getApplicationContext());
            error.setMessage("@string/PassError");
            error.setPositiveButton("OK", View.OnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    error.setCancelable(true);
                }
            }));
                error.create().show();
        }*/


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.hide();

                        try {
                            JSONObject json = new JSONObject(response);
                            String str = json.getString("status");
                            if(str.equals("success"))
                            {
                                Intent intent = new Intent(SignUp.this,MainActivity.class);
                                startActivity(intent);
                            }
                            Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            pd.hide();
                            Toast.makeText(SignUp.this, "error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.hide();
                        Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", Username);
                params.put("password", Password);
                params.put("email", emailsignup);
                params.put("contact", Contact);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finish();
    }
}
