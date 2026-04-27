package com.example.smarthome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class DoorActivity extends AppCompatActivity {

    TextView txtDoorStatus;
    Button btnToggleDoor;
    DatabaseReference doorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);

        txtDoorStatus = findViewById(R.id.txtDoorStatus);
        btnToggleDoor = findViewById(R.id.btnToggleDoor);

        doorRef = FirebaseDatabase.getInstance().getReference("smarthome/door");

        doorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);

                if ("OPEN".equals(state)) {
                    txtDoorStatus.setText("Door Status: OPEN 🚪");
                    btnToggleDoor.setText("CLOSE Door");
                    btnToggleDoor.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    txtDoorStatus.setText("Door Status: CLOSE");
                    btnToggleDoor.setText("OPEN Door");
                    btnToggleDoor.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        btnToggleDoor.setOnClickListener(v -> toggleDoor());
    }

    private void toggleDoor() {
        doorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                doorRef.setValue("OPEN".equals(state) ? "CLOSE" : "OPEN");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
