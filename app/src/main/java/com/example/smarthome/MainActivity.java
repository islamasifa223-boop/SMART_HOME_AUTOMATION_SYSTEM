package com.example.smarthome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import android.content.Intent;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btnLight, btnFan, btnDoor;
    TextView txtTemp, txtGas, txtSmoke;

    // Drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    DatabaseReference lightRef, fanRef, doorRef, tempRef, gasRef, smokeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🔹 Material Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 🔹 Linking UI
        btnLight = findViewById(R.id.btnLight);
        btnFan   = findViewById(R.id.btnFan);
        btnDoor  = findViewById(R.id.btnDoor);

        txtTemp  = findViewById(R.id.txtTemp);
        txtGas   = findViewById(R.id.txtGas);
        txtSmoke = findViewById(R.id.txtSmoke);

        // 🔹 Drawer layout + NavigationView
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // 🔹 Drawer Toggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 🔹 Menu click listener
        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_light) {
                startActivity(new Intent(MainActivity.this, LightActivity.class));
            }
            else if (id == R.id.menu_fan) {
                startActivity(new Intent(MainActivity.this, FanActivity.class));
            }
            else if (id == R.id.menu_door) {
                startActivity(new Intent(MainActivity.this, DoorActivity.class));
            }
            else if (id == R.id.menu_temp) {
                startActivity(new Intent(MainActivity.this, TemperatureActivity.class));
            }
            else if (id == R.id.menu_gas) {
                startActivity(new Intent(MainActivity.this, GasActivity.class));
            }
            else if (id == R.id.menu_smoke) {
                startActivity(new Intent(MainActivity.this, SmokeActivity.class));
            }

            drawerLayout.closeDrawers();
            return true;
        });


        // 🔹 Firebase references
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        lightRef = db.getReference("smarthome/light");
        fanRef   = db.getReference("smarthome/fan");
        doorRef  = db.getReference("smarthome/door");
        tempRef  = db.getReference("smarthome/temp");
        gasRef   = db.getReference("smarthome/gas");
        smokeRef = db.getReference("smarthome/smoke");

        // 🔥 Listeners (Temperature, Light, Fan, Door, Gas, Smoke)
        setupListeners();

        // 🔘 Toggle buttons
        btnLight.setOnClickListener(v -> toggleState(lightRef, "ON", "OFF"));
        btnFan.setOnClickListener(v -> toggleState(fanRef, "ON", "OFF"));
        btnDoor.setOnClickListener(v -> toggleState(doorRef, "OPEN", "CLOSE"));
    }

    private void setupListeners() {
        // Temperature listener
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer temp = snapshot.getValue(Integer.class);
                if (temp == null) { txtTemp.setText("Temperature: 25°C"); return; }
                txtTemp.setText("Temperature: " + temp + "°C");
                if (temp >= 30)
                    txtTemp.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                else if (temp >= 25)
                    txtTemp.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                else
                    txtTemp.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Light listener
        lightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                if ("ON".equals(state)) {
                    btnLight.setText("Turn OFF Light");
                    btnLight.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    btnLight.setText("Turn ON Light");
                    btnLight.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Fan listener
        fanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                if ("ON".equals(state)) {
                    btnFan.setText("Turn OFF Fan");
                    btnFan.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    btnFan.setText("Turn ON Fan");
                    btnFan.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Door listener
        doorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                if ("OPEN".equals(state)) {
                    btnDoor.setText("Close Door");
                    btnDoor.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    btnDoor.setText("Open Door");
                    btnDoor.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Gas listener
        gasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gas = snapshot.getValue(String.class);
                if ("DANGER".equals(gas)) {
                    txtGas.setText("GAS : DANGER ⚠");
                    txtGas.setBackgroundResource(R.drawable.bg_danger);
                    Toast.makeText(MainActivity.this,"⚠ GAS LEAK DETECTED!", Toast.LENGTH_LONG).show();
                } else {
                    txtGas.setText("GAS : SAFE");
                    txtGas.setBackgroundResource(R.drawable.bg_safe);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Smoke listener
        smokeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String smoke = snapshot.getValue(String.class);
                if ("DANGER".equals(smoke)) {
                    txtSmoke.setText("SMOKE : DANGER 🚨");
                    txtSmoke.setBackgroundResource(R.drawable.bg_danger);
                    Toast.makeText(MainActivity.this,"🚨 SMOKE DETECTED!", Toast.LENGTH_LONG).show();
                } else {
                    txtSmoke.setText("SMOKE : SAFE");
                    txtSmoke.setBackgroundResource(R.drawable.bg_safe);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void toggleState(DatabaseReference ref, String state1, String state2) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String current = snapshot.getValue(String.class);
                if (current == null || current.equals(state2)) {
                    ref.setValue(state1);
                } else {
                    ref.setValue(state2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,
                        "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hamburger click handle
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
}
