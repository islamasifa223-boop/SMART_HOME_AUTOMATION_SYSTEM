package com.example.smarthome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LightActivity extends AppCompatActivity {

    TextView txtLightStatus;
    Button btnToggleLight;

    DatabaseReference lightRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        // 🔹 Linking UI
        txtLightStatus = findViewById(R.id.txtLightStatus);
        btnToggleLight = findViewById(R.id.btnToggleLight);

        // 🔹 Firebase reference
        lightRef = FirebaseDatabase.getInstance().getReference("smarthome/light");

        // 🔹 Listener to update UI
        lightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                if ("ON".equals(state)) {
                    txtLightStatus.setText("Light Status: ON 💡");
                    btnToggleLight.setText("Turn OFF Light");
                    btnToggleLight.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                } else {
                    txtLightStatus.setText("Light Status: OFF");
                    btnToggleLight.setText("Turn ON Light");
                    btnToggleLight.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LightActivity.this, "Firebase Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 🔹 Toggle button click
        btnToggleLight.setOnClickListener(v -> toggleLight());
    }

    private void toggleLight() {
        lightRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String current = snapshot.getValue(String.class);
                if (current == null || current.equals("OFF")) {
                    lightRef.setValue("ON");
                } else {
                    lightRef.setValue("OFF");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LightActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
