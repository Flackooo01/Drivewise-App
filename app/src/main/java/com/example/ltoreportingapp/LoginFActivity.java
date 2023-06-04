package com.example.ltoreportingapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;


public class LoginFActivity extends AppCompatActivity {

    TextView textViewtv_havent_account;
    TextInputEditText textInputEditTextusername;
    TextInputEditText textInputEditTextpassword;
    AppCompatButton LoginBtn;
    String Id, Fullname, Email, Username, Password;
    TextView textViewError;
    GifImageView progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_factivity);

        textInputEditTextusername = findViewById(R.id.et_username);
        textInputEditTextpassword = findViewById(R.id.et_password);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        LoginBtn = findViewById(R.id.LoginBtn);
        textViewtv_havent_account = findViewById(R.id.tv_havent_account);
        sharedPreferences = getSharedPreferences("DriveWise", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("true")) {
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
            finish();
        }

        LoginBtn.setOnClickListener(v -> {
            Username = textInputEditTextusername.getText().toString().trim();
            Password = textInputEditTextpassword.getText().toString().trim();

            if (!Username.isEmpty() || !Password.isEmpty()) {
                Login(Username, Password);
            }else {
                Toast.makeText(LoginFActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            }

        });
        textViewtv_havent_account.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), RegistrationFActivity.class);
            startActivity(intent1);
            finish();
        });

    }

    private void Login(String Username, String Password) {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.100.9/Drivewise-Main/DatabaseV2/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");
                        jsonObject.getString("message");
                        if(success.equals("1")){
                            for (int i =0; i < jsonArray.length(); i++){
                                Toast.makeText(LoginFActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                JSONObject object = jsonArray.getJSONObject(i);
                                String Fullname = object.getString("Fullname").trim();
                                String Email = object.getString("Email").trim();
                                String ID = object.getString("ID").trim();
                                String Usernames = object.getString("Username").trim();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged", "true");
                                editor.putString("ID", ID);
                                editor.putString("Fullname", Fullname);
                                editor.putString("Email", Email);
                                editor.putString("Username", Usernames);
                                editor.apply();
                                Intent intent = new Intent(LoginFActivity.this, HomepageActivity.class);
                                startActivity(intent);
                                finish();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginFActivity.this, "Username And Password is incorrect", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(LoginFActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            textViewError.setText(error.getLocalizedMessage());
            textViewError.setVisibility(View.VISIBLE);
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("Username", Username);
                paramV.put("Password", Password);
                return paramV;
            }
        };
        queue.add(stringRequest);

    }
}