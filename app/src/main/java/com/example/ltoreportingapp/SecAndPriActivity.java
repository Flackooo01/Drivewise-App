package com.example.ltoreportingapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;

public class SecAndPriActivity extends AppCompatActivity {

    private SwitchCompat switchFingerprint;
    private FingerprintManager fingerprintManager;
    private SwitchCompat switchEncryption;

    //Remote Wipe
    private SwitchCompat switchRemoteWipe;

    // back btn
    ImageView Backbtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_and_pri);
        //fingerprint
        switchFingerprint = findViewById(R.id.switch_fingerprint);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        //Encrypt
        switchEncryption = findViewById(R.id.switch_encryption);

        //fingerprint function
        switchFingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 // enable or disable fingerprint login based on switch state
                if (b) {
                    if (fingerprintManager.isHardwareDetected()) {
                        if (fingerprintManager.hasEnrolledFingerprints()) {
                            // Start listening for fingerprint authentication
                        } else {
                            Toast.makeText(SecAndPriActivity.this, "No fingerprints registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SecAndPriActivity.this, "Fingerprint sensor not detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Stop listening for fingerprint authentication
                }
            }
        });

        //Encryption function
        switchEncryption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Enable or disable encryption based on the switch state
                if (b) {
                    try {
                        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                        keyStore.load(null);
                        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                        keyGenerator.init(new KeyGenParameterSpec.Builder("encryption_key", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                                .setUserAuthenticationRequired(true)
                                .build());
                        keyGenerator.generateKey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SecAndPriActivity.this, "Enable Encryption", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                        keyStore.load(null);
                        keyStore.deleteEntry("encryption_key");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SecAndPriActivity.this, "Disable Encryption", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Remote Wipe
            switchRemoteWipe = findViewById(R.id.switch_remote_wipe);
            switchRemoteWipe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Remote wipe is enabled
                        Toast.makeText(SecAndPriActivity.this, "Enable Remote wipe", Toast.LENGTH_SHORT).show();

                    } else {
                        // Remote wipe is disabled
                        Toast.makeText(SecAndPriActivity.this, "Disable Remote wipe", Toast.LENGTH_SHORT).show();
                    }
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