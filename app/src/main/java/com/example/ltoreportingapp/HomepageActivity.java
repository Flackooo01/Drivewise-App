package com.example.ltoreportingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ltoreportingapp.databinding.ActivityHomepageBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.AccessController;

import kotlin.jvm.internal.Intrinsics;

public class HomepageActivity extends AppCompatActivity{

    ActivityHomepageBinding binding;
    private AccessController view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        BottomAppBar bottomAppBar = findViewById(R.id.bottomappbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);

        binding.bottomnav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.btn_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.btn_list:
                    replaceFragment(new violationlist());
                    break;
                case R.id.btn_history:
                    Intent intent2 = new Intent(this,History.class);
                    this.startActivity(intent2);
                    finish();
                    break;
                case R.id.btn_settings:
                    Intent intent1 = new Intent(this,SettingActivity.class);
                    this.startActivity(intent1);
                    finish();
                    break;
            }
            return true;
        });

        BottomNavigationView var10000 = (BottomNavigationView)this.findViewById(R.id.bottomnav);
        Intrinsics.checkNotNullExpressionValue(var10000, "bottomNavigatorView");
        MenuItem var3 = var10000.getMenu().getItem(2);
        Intrinsics.checkNotNullExpressionValue(var3, "bottomNavigatorView.menu.getItem(2)");
        var3.setEnabled(false);

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportpageActivity.class);
            startActivity(intent);
            finish();
        });

        int height = bottomNavigationView.getHeight();

//Hide
        bottomNavigationView.clearAnimation();
        bottomNavigationView.animate().translationY(height).setDuration(200);
//Show
        bottomNavigationView.clearAnimation();
        bottomNavigationView.animate().translationY(0).setDuration(200);

        int height2 = bottomAppBar.getHeight();

//Hide
        bottomAppBar.clearAnimation();
        bottomAppBar.animate().translationY(height2).setDuration(200);
//Show
        bottomAppBar.clearAnimation();
        bottomAppBar.animate().translationY(0).setDuration(200);

        int height3 = floatingActionButton.getHeight();

//Hide
        floatingActionButton.clearAnimation();
        floatingActionButton.animate().translationY(height3).setDuration(200);
//Show
        floatingActionButton.clearAnimation();
        floatingActionButton.animate().translationY(0).setDuration(200);

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}
 