package com.wlksilvestre.backgroundtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btStart = findViewById(R.id.btIniciarProcesso);
        Button btStart2 = findViewById(R.id.btIniciarProcesso2);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("INFO", "botão pressionado");
                startBackgroundProcessButtonClick(view);
            }
        });

        btStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("INFO", "botão pressionado");
            }
        });
    }

    public void startBackgroundProcessButtonClick (View view) {
        Log.e("INFO", "Função iniciada");

        Intent intent = new Intent(this, myBackgroundProcess.class);
        intent.setAction("BackgroundProcess_01");

        Log.e("INFO", "Intent iniciado");

        // Set the repeated Task

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Log.e("INFO", "Pènding intent");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent);
        Log.e("INFO", "alarm no final da função");
    }

}






















