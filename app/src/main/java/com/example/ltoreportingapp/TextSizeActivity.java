package com.example.ltoreportingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TextSizeActivity extends AppCompatActivity {
    //text size code
    private TextView txtTitle;
    private SeekBar seekbarTextSize;
    private TextView txtTextSizeValue;
    private Button btnSave;

    // back btn
    ImageView Backbtn;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_size);

        //text size code
        txtTitle = findViewById(R.id.txt_title);
        seekbarTextSize = findViewById(R.id.seekbar_text_size);
        txtTextSizeValue = findViewById(R.id.txt_text_size_value);
        btnSave = findViewById(R.id.btn_save);

        seekbarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtTextSizeValue.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //do nothing
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textSize = seekbarTextSize.getProgress();
                // Save the selected text size to shared preferences or another storage method
                SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("text_size", textSize);
                editor.apply();
                // Show a message to the user that the text size has been saved
                Toast.makeText(TextSizeActivity.this, "Text size saved", Toast.LENGTH_SHORT).show();
                // Close the activity or navigate to another screen
                finish();
            }
        });


        Backbtn = findViewById(R.id.backbtn);

        // back btn
        Backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            finish();
        });
    }
}