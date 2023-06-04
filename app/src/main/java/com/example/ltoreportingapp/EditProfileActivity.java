package com.example.ltoreportingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    ImageView backbtn;
    TextInputEditText Edit_Username,Edit_Password,Edit_Confirm_Password;
    TextView id,email,textviewerror;

    AppCompatButton Edit_Button;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ProgressDialog progressDialog = null;

        //shared preference
        id = findViewById(R.id.textname);
        email = findViewById(R.id.textemail);

        textviewerror = findViewById(R.id.error);
        Edit_Button = findViewById(R.id.Edit_Button);
        backbtn = findViewById(R.id.backbtn);
        Edit_Username = findViewById(R.id.Edit_Username);
        Edit_Password = findViewById(R.id.Edit_Password);
        Edit_Confirm_Password = findViewById(R.id.Edit_Confirm_Password);
        sharedPreferences = getSharedPreferences("DriveWise", MODE_PRIVATE);

        //get the string
        Intent i = getIntent();
        final String mUsername = i.getStringExtra("Username");
        Edit_Username.setText(mUsername);

        if (sharedPreferences.getString("logged", "false").equals("false")){
            Intent intent = new Intent(getApplicationContext(), LoginFActivity.class);
            startActivity(intent);
            finish();
        }


        id.setText(sharedPreferences.getString("Username", ""));
        email.setText(sharedPreferences.getString("Email", ""));

        Edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String Username = Edit_Username.getText().toString().trim();
                final String Password = Edit_Password.getText().toString().trim();
                final String Confirm_Password = Edit_Confirm_Password.getText().toString().trim();

                 if(Username.isEmpty() || Password.isEmpty() || Confirm_Password.isEmpty()){
                     Toast.makeText(EditProfileActivity.this, "Some Fields are empty", Toast.LENGTH_SHORT).show();
                 }else{

                     RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                     String url = "http://192.168.100.9/Drivewise-Main/DatabaseV2/editprofile.php";

                     StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             textviewerror.setText(response);
                             textviewerror.setVisibility(View.VISIBLE);
                             Toast.makeText(EditProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(EditProfileActivity.this, SettingActivity.class);
                             startActivity(intent);
                             finish();
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {

                             textviewerror.setText(error.getLocalizedMessage());
                             textviewerror.setVisibility(View.VISIBLE);
                         }
                     }){
                         protected Map<String, String> getParams(){
                             Map<String, String> UpdateparamV = new HashMap<>();
                             UpdateparamV.put("ID", sharedPreferences.getString("ID", ""));
                             UpdateparamV.put("Username", Username);
                             UpdateparamV.put("Password", Password);
                             UpdateparamV.put("Confirm_Password", Confirm_Password);



                             return UpdateparamV;
                         }
                     };
                     queue.add(stringRequest);
                 }

            }
        });



        backbtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            finish();
        });
    }
}