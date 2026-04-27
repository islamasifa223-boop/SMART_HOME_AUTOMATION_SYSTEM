package com.example.smarthome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class FanActivity extends AppCompatActivity {

    TextView txtFanStatus;
    Button btnToggleFan;
    DatabaseReference fanRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);

        txtFanStatus = findViewById(R.id.txtFanStatus);
        btnToggleFan = findViewById(R.id.btnToggleFan);

        fanRef = FirebaseDatabase.getInstance().getReference("smarthome/fan");

        fanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);

                if ("ON".equals(state)) {
                    txtFanStatus.setText("Fan Status: ON 🌀");
                    btnToggleFan.setText("Turn OFF Fan");
                    btnToggleFan.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                } else {
                    txtFanStatus.setText("Fan Status: OFF");
                    btnToggleFan.setText("Turn ON Fan");
                    btnToggleFan.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnToggleFan.setOnClickListener(v -> toggleFan());
    }

    private void toggleFan() {
        fanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                fanRef.setValue("ON".equals(state) ? "OFF" : "ON");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
