package com.example.ltoreportingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FAQsActivity extends AppCompatActivity {
    // back btn
    ImageView Backbtn;
    RecyclerView recyclerView;
    List<Versions> versionsList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        Backbtn = findViewById(R.id.backbtn);

        // back btn
        Backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);

        initData();
        setRecyclerView();
    }

    private void setRecyclerView() {
        VersionsAdapter versionsAdapter = new VersionsAdapter(versionsList);
        recyclerView.setAdapter(versionsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {

        versionsList = new ArrayList<>();
        versionsList.add(new Versions("1. Is the app free to download and use?","Answer:","","The app may be free to download but it could have in-app purchases or subscription fees for certain features or content."));
        versionsList.add(new Versions("2. Are there any in-app purchases or subscription fees?","Answer:","","It depends on the app, some apps may offer in-app purchases or subscription fees for certain features or content, while others may not."));
        versionsList.add(new Versions("3. How do I reset my password?","Answer:","","You can reset your password by clicking on \"Forgot Password\" on the login page and providing your registered email address. A link to reset your password will be sent to your email."));
        versionsList.add(new Versions("4. How do I navigate through the app?","Answer:","","The navigation structure of the app may vary, but generally you can use the navigation bar or menu located at the bottom or top of the screen to access different sections of the app."));
        versionsList.add(new Versions("5. How do I report a bug or technical issue?","Answer:","","You can report a bug or technical issue by contacting the support team through the in-app support center or by email."));


    }
}