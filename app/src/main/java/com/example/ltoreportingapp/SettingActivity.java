package com.example.ltoreportingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity<relativeLayoutSecAndPri> extends AppCompatActivity {

    ImageView Backbtn;
    TextView textViewname;
    TextView textViewemail;
    RelativeLayout relativeLayoutlogout;
    SharedPreferences sharedPreferences;
    CircleImageView circleImageViewimageBtn;

    Button update;
    //Dark mode and light mode
    SwitchCompat switcher;
    boolean nightMODE;
    SharedPreferences shrdPreferences;
    SharedPreferences.Editor editor;

    // Notification switch
    SwitchCompat notificationSwitch;

    // Sec and Privacy
    private RelativeLayout relativeLayoutSecAndPri;

    //Text Size-----
    private RelativeLayout relativeLayoutTextSize;

    //Language-----
    private RelativeLayout relativeLayoutLanguage;

    //Policy
    private RelativeLayout relativeLayoutPolicy;

    //ABOUT------
    private RelativeLayout relativeLayoutAbout;

    //FAQs----
    private RelativeLayout relativeLayoutFAQs;

    String getId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Backbtn = findViewById(R.id.backbtn);
        circleImageViewimageBtn = findViewById(R.id.imageBtn);
        circleImageViewimageBtn = findViewById(R.id.imageBtn);
        textViewname = findViewById(R.id.textname);
        textViewemail = findViewById(R.id.textemail);
        relativeLayoutlogout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences("DriveWise", MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "false").equals("false")){
            Intent intent = new Intent(getApplicationContext(), LoginFActivity.class);
            startActivity(intent);
            finish();
        }

        textViewname.setText(sharedPreferences.getString("Fullname", ""));
        textViewemail.setText(sharedPreferences.getString("Email", ""));

        relativeLayoutlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.100.9/Drivewise-Main/DatabaseV2/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            if (response.equals("success")) {
                                Toast.makeText(SettingActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("logged", "");
                                editor.putString("ID", "");
                                editor.putString("Username", "");
                                editor.putString("Email", "");
                                editor.putString("Active", "");
                                editor.apply();
                                Intent intent = new Intent(SettingActivity.this, LoginFActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(SettingActivity.this, response, Toast.LENGTH_SHORT).show();
                        }, Throwable::printStackTrace) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("Fullname", sharedPreferences.getString("Fullname", ""));
                        paramV.put("Active", sharedPreferences.getString("Active", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });


        Backbtn.setOnClickListener(v -> {
            Intent in = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(in);
            finish();
        });

        //Dark mode and light mode
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        switcher = findViewById(R.id.night);
        // we used sharedPreference to save mode if exit the app and go back again
        shrdPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = shrdPreferences.getBoolean("night", false); // light mode is the default mode

        if (nightMODE){
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            switcher.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = shrdPreferences.edit();
                    editor.putBoolean("night", false);
                    Toast.makeText(SettingActivity.this, "Light Mode", Toast.LENGTH_SHORT).show();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = shrdPreferences.edit();
                    editor.putBoolean("night", true);
                    Toast.makeText(SettingActivity.this, "Night Mode", Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });

        // Notification switch
        notificationSwitch = findViewById(R.id.notification_switch);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // Enable notifications
                    enableNotifications();
                    Toast.makeText(SettingActivity.this, "Enable notifications", Toast.LENGTH_SHORT).show();
                } else {
                    // Disable notifications
                    disableNotifications();
                    Toast.makeText(SettingActivity.this, "Disable notifications", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Sec and Privacy
        relativeLayoutSecAndPri = (RelativeLayout) findViewById(R.id.SecAndPri);
        relativeLayoutSecAndPri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //Text Size-----
        relativeLayoutTextSize = (RelativeLayout) findViewById(R.id.TextSizeClick);
        relativeLayoutTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTextSizeActivity();
            }
        });

        //Language------
        relativeLayoutLanguage = (RelativeLayout) findViewById(R.id.LanguageClick);
        relativeLayoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLanguageActivity();
            }
        });

        //Policy
        relativeLayoutPolicy = (RelativeLayout) findViewById(R.id.PolicyCLick);
        relativeLayoutPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPolicyActivity();
            }
        });

        //ABOUT------
        relativeLayoutAbout = (RelativeLayout) findViewById(R.id.AboutClick);
        relativeLayoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAboutActivity();
            }
        });

        //FAQs-------
        relativeLayoutFAQs = (RelativeLayout) findViewById(R.id.FAQsClick);
        relativeLayoutFAQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFAQsActivity();
            }
        });
    }

    //notification Implement the enableNotifications() and disableNotifications() methods.
    private void enableNotifications() {
        // Register for notifications and show any necessary UI
    }

    private void disableNotifications() {
        // Unregister for notifications and hide any necessary UI
    }

    //Sec and Pri
    public void openSecurityAndPrivacy(){
        Intent SecAndPriIntent = new Intent(this, SecAndPriActivity.class);
        startActivity(SecAndPriIntent);
    }

    //Text size----
    public void openTextSizeActivity(){
        Intent TxtSizeIntent = new Intent(this, TextSizeActivity.class);
        startActivity(TxtSizeIntent);
    }

    //Language----
    public void openLanguageActivity(){
        Intent langIntent = new Intent(this, LanguageActivity.class);
        startActivity(langIntent);
    }

    //Policy
    public void openPolicyActivity(){
        Intent PolicyIntent = new Intent(this, PolicyActivity.class);
        startActivity(PolicyIntent);
    }

    //about----
    public void openAboutActivity () {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    //FAQs----
    public void openFAQsActivity() {
        Intent faqsIntent = new Intent(this, FAQsActivity.class);
        startActivity(faqsIntent);
    }
}

