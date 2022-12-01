package com.wlksilvestre.backgroundtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class myBackgroundProcess extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("INFO BG", "// Função iniciada");
        Ringtone ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        ringtone.play();

        Log.e("INFO BG", "// Tocando o ringtones");

        Toast.makeText(context,"Processo em segundo plano executado" + Calendar.getInstance().toString(), Toast.LENGTH_SHORT).show();


        Log.e("INFO BG", "// Pausando 2000 ms");
        SystemClock.sleep(2000);


        Log.e("INFO BG", "// Vai parar");
        ringtone.stop();
    }
}
