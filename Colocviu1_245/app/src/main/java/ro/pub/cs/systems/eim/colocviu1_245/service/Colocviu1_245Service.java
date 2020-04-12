package ro.pub.cs.systems.eim.colocviu1_245.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Colocviu1_245Service extends Service {

    private ProcessingThread processingThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int suma = intent.getIntExtra("suma_calculata", -1);
        String data_si_ora = intent.getStringExtra("data_si_ora");
        processingThread = new ProcessingThread(this, suma, data_si_ora);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }
}
