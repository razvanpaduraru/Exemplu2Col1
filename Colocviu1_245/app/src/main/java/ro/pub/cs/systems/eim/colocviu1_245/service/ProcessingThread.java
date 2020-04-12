package ro.pub.cs.systems.eim.colocviu1_245.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;
import java.util.Random;


public class ProcessingThread extends Thread {
    private Context context;
    private boolean isRunning = true;

    private int suma;
    private String data_si_ora;

    public ProcessingThread(Context context, int suma, String data_si_ora) {
        this.context = context;
        this.suma = suma;
        this.data_si_ora = data_si_ora;
    }

    @Override
    public void run() {
        Log.d("[Thread Tag]", "Thread has started!");
        while (isRunning) {
            sendMessage();
            sleep();
        }
        Log.d("[Thread Tag]", "Thread has stopped!");
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction("threadSuma");
        intent.putExtra("message",
                data_si_ora + " si suma este : " + suma);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
