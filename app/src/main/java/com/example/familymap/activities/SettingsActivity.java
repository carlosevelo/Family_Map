package com.example.familymap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymap.R;
import com.example.familymap.cache.DataCache;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        DataCache data = DataCache.getInstance();

        SwitchMaterial lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        SwitchMaterial familyLinesSwitch = findViewById(R.id.familyLinesSwitch);
        SwitchMaterial spouseLinesSwitch = findViewById(R.id.spouseLinesSwitch);
        SwitchMaterial fatherSideSwitch = findViewById(R.id.fatherSideSwitch);
        SwitchMaterial motherSideSwitch = findViewById(R.id.motherSideSwitch);
        SwitchMaterial maleEventsSwitch = findViewById(R.id.maleEventsSwitch);
        SwitchMaterial femaleEventsSwitch = findViewById(R.id.femaleEventsSwitch);
        LinearLayout logoutButton = findViewById(R.id.logoutButton);

        lifeStorySwitch.setChecked(data.isStoryLine());
        familyLinesSwitch.setChecked(data.isFamilyLine());
        spouseLinesSwitch.setChecked(data.isSpouseLine());
        fatherSideSwitch.setChecked(data.isFatherSide());
        motherSideSwitch.setChecked(data.isMotherSide());
        maleEventsSwitch.setChecked(data.isMaleEvents());
        femaleEventsSwitch.setChecked(data.isFemaleEvents());

        lifeStorySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setStoryline(isChecked));
        familyLinesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setFamilyLine(isChecked));
        spouseLinesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setSpouseLine(isChecked));
        fatherSideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setFatherSide(isChecked));
        motherSideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setMotherSide(isChecked));
        maleEventsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setMaleEvents(isChecked));
        femaleEventsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> data.setFemaleEvents(isChecked));
        logoutButton.setOnClickListener(v -> {
            data.setAuthToken(null);
            data.ClearDataCache();

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}