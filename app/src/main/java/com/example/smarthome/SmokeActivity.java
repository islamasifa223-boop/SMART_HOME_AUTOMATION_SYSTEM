package com.example.smarthome;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SmokeActivity extends AppCompatActivity {

    TextView txtSmokeStatus;
    DatabaseReference smokeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke);

        txtSmokeStatus = findViewById(R.id.txtSmokeStatus);

        // Firebase reference
        smokeRef = FirebaseDatabase.getInstance().getReference("smarthome/smoke");

        smokeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                if ("DANGEROUS".equalsIgnoreCase(state)) {
                    txtSmokeStatus.setText("Smoke Status: DANGEROUS ⚠️");
                    txtSmokeStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    txtSmokeStatus.setText("Smoke Status: SAFE ✅");
                    txtSmokeStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SmokeActivity.this, "Firebase Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
