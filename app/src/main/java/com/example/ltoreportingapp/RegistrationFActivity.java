package com.example.ltoreportingapp;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class RegistrationFActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextfullname;
    TextInputEditText textInputEditTextemail;
    TextInputEditText textInputEditTextusername;
    TextInputEditText textInputEditTextpassword;
    TextInputEditText textInputEditTextconfirm_password;
    TextView LBtn;
    AppCompatButton btn_register;
    String Fullname, Email, Username, Password, Confirm_Password,success;
    TextView textViewError;
    GifImageView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_factivity);
        textInputEditTextfullname = findViewById(R.id.et_fullname);
        textInputEditTextemail = findViewById(R.id.et_email);
        textInputEditTextusername = findViewById(R.id.et_username);
        textInputEditTextpassword = findViewById(R.id.et_password);
        textInputEditTextconfirm_password = findViewById(R.id.et_confirm_password);
        btn_register = findViewById(R.id.btn_register);
        textViewError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);
        LBtn = findViewById(R.id.tv_have_account);

        btn_register.setOnClickListener(v -> {
            Regist();

        });
        LBtn.setOnClickListener(v -> {
            Intent i = new Intent(RegistrationFActivity.this, LoginFActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void Regist(){
        progressBar.setVisibility(View.GONE);

        Fullname = this.textInputEditTextfullname.getText().toString().trim();
        Email = this.textInputEditTextemail.getText().toString().trim();
        Username = this.textInputEditTextusername.getText().toString();
        Password = this.textInputEditTextpassword.getText().toString().trim();
        Confirm_Password = this.textInputEditTextconfirm_password.getText().toString().trim();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.100.9/Drivewise-Main/DatabaseV2/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        success = jsonObject.getString("success");
                        if(success.equals("1")) {
                            Toast.makeText(RegistrationFActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegistrationFActivity.this, LoginFActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegistrationFActivity.this, response, Toast.LENGTH_SHORT).show();
                    }

                }, error -> {
            Toast.makeText(RegistrationFActivity.this, "Register Error" + error.toString(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            textViewError.setText(error.getLocalizedMessage());
            textViewError.setVisibility(View.VISIBLE);
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("Fullname", Fullname);
                paramV.put("Email", Email);
                paramV.put("Username", Username);
                paramV.put("Password", Password);
                paramV.put("Confirm_Password", Confirm_Password);
                return paramV;
            }
        };
        queue.add(stringRequest);

    }

}
