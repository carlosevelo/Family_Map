package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.cache.DataCache;
import com.example.familymap.fragments.LoginFragment;
import com.example.familymap.fragments.MapFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {
    private final FragmentManager fm = this.getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);

        DataCache data = DataCache.getInstance();

        Fragment loginFragment = fm.findFragmentById(R.id.loginFragmentLayout);
        Fragment mapFragment = fm.findFragmentById(R.id.mapFragmentLayout);

        if (data.getAuthToken() == null) {
            if (loginFragment == null) {
                loginFragment = new LoginFragment();
                fm.beginTransaction()
                        .add(R.id.mainActivityLayout, loginFragment)
                        .commit();
            }
        }
        else {
            if (mapFragment == null) {
                mapFragment = new MapFragment();
                fm.beginTransaction()
                        .add(R.id.mainActivityLayout, mapFragment)
                        .commit();
            }
        }
    }

}