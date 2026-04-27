package com.example.smarthome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TemperatureActivity extends AppCompatActivity {

    TextView txtTemperatureStatus;
    EditText etTemperature;
    Button btnSetTemperature;

    DatabaseReference tempRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        txtTemperatureStatus = findViewById(R.id.txtTemperatureStatus);
        etTemperature = findViewById(R.id.txtTemperatureStatus);
        btnSetTemperature = findViewById(R.id.btnSave);

        // Firebase reference
        tempRef = FirebaseDatabase.getInstance()
                .getReference("smarthome/temperature");

        // Read temperature from Firebase
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double temp = snapshot.getValue(Double.class);

                if (temp != null) {
                    txtTemperatureStatus.setText(
                            "Current Temperature: " + temp + " °C 🌡️"
                    );
                } else {
                    txtTemperatureStatus.setText("Temperature not set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // Button click
        btnSetTemperature.setOnClickListener(v -> setTemperature());
    }

    private void setTemperature() {
        String tempText = etTemperature.getText().toString();

        if (tempText.isEmpty()) {
            Toast.makeText(this,
                    "Enter temperature", Toast.LENGTH_SHORT).show();
            return;
        }

        double temperature = Double.parseDouble(tempText);
        tempRef.setValue(temperature);

        Toast.makeText(this,
                "Temperature Updated", Toast.LENGTH_SHORT).show();

        etTemperature.setText("");
    }
}
