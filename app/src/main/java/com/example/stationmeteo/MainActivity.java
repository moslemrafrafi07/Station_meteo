package com.example.stationmeteo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {
    DatabaseReference dhtRef, bmpRef; // ref bmp and dht
    TextView tempDHT, humDHT, tempBMP, pressureBMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ref bmp and dht
        dhtRef = FirebaseDatabase.getInstance().getReference().child("dht11");
        bmpRef = FirebaseDatabase.getInstance().getReference().child("bmp280");

        // Init txt vw temp and hum dht
        tempDHT = findViewById(R.id.tempDHT);
        humDHT = findViewById(R.id.humDHT);

        // Init txt vw prssr and temp bmp
        tempBMP = findViewById(R.id.tempBMP);
        pressureBMP = findViewById(R.id.pressureBMP);

        // dht listener
        dhtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot temperatureSnapshot = dataSnapshot.child("temperature");
                DataSnapshot humiditySnapshot = dataSnapshot.child("humidity");

                String tempdata = temperatureSnapshot.getValue().toString();
                String humdata = humiditySnapshot.getValue().toString();
                tempDHT.setText(tempdata);
                humDHT.setText(humdata);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Failed to read value for DHT11.", error.toException());
            }
        });

        // listener BMP280
        bmpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot temperatureSnapshot = dataSnapshot.child("temperature");
                DataSnapshot pressureSnapshot = dataSnapshot.child("pressure");

                String tempdata = temperatureSnapshot.getValue().toString();
                String pressuredata = pressureSnapshot.getValue().toString();
                tempBMP.setText(tempdata);
                pressureBMP.setText(pressuredata);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Failed to read value for BMP280.", error.toException());
            }
        });
    }
}
