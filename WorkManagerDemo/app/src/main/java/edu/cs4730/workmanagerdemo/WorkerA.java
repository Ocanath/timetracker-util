package edu.cs4730.workmanagerdemo;

import android.content.Context;
import androidx.annotation.NonNull;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * very simple worker to fake some work and allow the MainActivity to observe.  It is intended to run
 * for 2 minutes (maybe shorter) so the UI can update and say its working.  but really nothing
 * interesting happens in here.
 *
 * Please note, workerA will survive ending this program, so when testing be very careful or you
 * will have many many many of them running as I found out.  They will be restarted with the new code
 * though.  hence all the logging here.
 */
public class WorkerA extends Worker {
    public WorkerA(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    Blaster mblaster = new Blaster(this.getApplicationContext());

    @NonNull
    @Override
    public Result doWork() {
        long duration = 20*60*1000;  //milliseconds
        long loopdelay = 30000; //milliseconds
        for(long iters = 0; iters < duration/loopdelay; iters++)
        {
            Log.d("workerA", "iteration "+iters);
            mblaster.blastMessage();
            try {
                Thread.sleep(loopdelay);
            }catch (InterruptedException e){
                return Result.failure();
            }
        }
        Log.d("workerA", "done.");
        return Result.success();
    }
}

