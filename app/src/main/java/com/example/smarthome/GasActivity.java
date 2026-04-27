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

public class GasActivity extends AppCompatActivity {

    TextView txtGasStatus;
    DatabaseReference gasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);

        txtGasStatus = findViewById(R.id.txtGasStatus);

        // Firebase reference
        gasRef = FirebaseDatabase.getInstance().getReference("smarthome/gas");

        gasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String state = snapshot.getValue(String.class);
                if ("DANGER".equalsIgnoreCase(state)) {
                    txtGasStatus.setText("Gas Status: DANGER ⚠️");
                    txtGasStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    txtGasStatus.setText("Gas Status: SAFE ✅");
                    txtGasStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GasActivity.this, "Firebase Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
