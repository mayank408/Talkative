package com.example.mayank.abcd;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayank on 30-11-2016.
 */

public class LoginRequest extends StringRequest {

    private static final String URL = "";
    private Map<String,String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener )
    {

        super(Method.POST,URL,listener,null);
        params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
