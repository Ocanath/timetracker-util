package edu.cs4730.workmanagerdemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import edu.cs4730.workmanagerdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Blaster mBlaster = new Blaster(this);

        //set the click listeners for the three buttons.
        binding.btnOneshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneshot();
            }
        });

        binding.btnBlastem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBlaster.toggleLights();
            }
        });
    }

    /**
     * This will create a oneshot workerA task and schedule it to run once.
     * commented out code shows how to make it recur every 24 hours.
     */
    public void oneshot() {
        //for a schedule once
        //OneTimeWorkRequest runWorkA = new OneTimeWorkRequest.Builder(WorkerA.class).build();
        /*
        //for recur schedule, say every 24 hours, comment out above, since duplicate variables.
        PeriodicWorkRequest.Builder workerkBuilder =
            new PeriodicWorkRequest.Builder(WorkerA.class, 24,  TimeUnit.HOURS);
        // ...if you want, you can apply constraints to the builder here...
        Constraints myConstraints = new Constraints.Builder()
            //.setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            // Many other constraints are available, see the
            // Constraints.Builder reference
            .build();

       // Create the actual work object:
        PeriodicWorkRequest runWorkA = workerkBuilder.setConstraints(myConstraints)
        .build();
       */
        PeriodicWorkRequest.Builder workerkBuilder = new PeriodicWorkRequest.Builder(WorkerA.class, 20,  TimeUnit.MINUTES);
        Constraints myConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest runWorkA = workerkBuilder.setConstraints(myConstraints).build();

        //now enqueue the task so it can be run.
        WorkManager.getInstance(getApplicationContext()).enqueue(runWorkA);

        //not necessary, but this will tell us the status of the task.
        LiveData<WorkInfo> status = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(runWorkA.getId());
        status.observe(this, new Observer<WorkInfo>() {

            @Override
            public void onChanged(@Nullable WorkInfo workStatus) {
                switch (workStatus.getState()) {
                    case BLOCKED:
                        binding.tvOneshot.setText("Status is Blocked");
                        break;
                    case CANCELLED:
                        binding.tvOneshot.setText("Status is canceled");
                        break;
                    case ENQUEUED:
                        binding.tvOneshot.setText("Status is enqueued");
                        break;
                    case FAILED:
                        binding.tvOneshot.setText("Status is failed");
                        break;
                    case RUNNING:
                        binding.tvOneshot.setText("Status is running");
                        break;
                    case SUCCEEDED:
                        binding.tvOneshot.setText("Status is succeeded");
                        break;
                    default:
                        binding.tvOneshot.setText("Status is unknown");
                }

            }
        });
    }
}
