package edu.cs4730.workmanagerdemo;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.net.DatagramSocket;
import java.net.InetAddress;

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

    private DatagramSocket soc;
    private InetAddress addr;
    private int port;

    @NonNull
    @Override
    public Result doWork() {
        Log.d("workerA", "WAZZAAAAAP");
        return Result.success();
    }
}
