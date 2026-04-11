package com.iplogger;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import java.lang.StringBuilder;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // List to store up to 10 IPv4 addresses
    private TextView ipDisplayTextView;

    // BroadcastReceiver to listen for network changes
    public BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get the connectivity manager service
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            // Check if connected to Wi-Fi
            if (networkInfo != null && networkInfo.isConnected()) {
                // Log the IPv4 address when Wi-Fi is connected
                Helper.logIPv4Address(context);
                displayLastIp();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TextView to display IP addresses
        ipDisplayTextView = findViewById(R.id.ip_display);
        scheduleIPLoggerJob();
        Helper.loadIpList(this);
        displayLastIp();

        // Register the BroadcastReceiver for Wi-Fi connection state changes
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver to prevent memory leaks
        unregisterReceiver(wifiReceiver);
    }

    private void displayLastIp() {
        if (Helper.ipList.isEmpty()) {
            // Display "null" if the list is empty (initial state)
            ipDisplayTextView.setText(R.string.no_value); // Use the string resource
        } else {
            int size = Helper.ipList.size();
            // Display the last IP in the list
            StringBuilder lastip = new StringBuilder();
            for (int i = size - 1; i >= 0; i--) {
                lastip.append(Helper.ipList.get(i))
                        .append(" - ")
                        .append(Helper.getblock(Helper.ipList.get(i)))
                        .append("\n\n");
            }
            ipDisplayTextView.setText(lastip.toString().trim());

        }
    }

    private void scheduleIPLoggerJob() {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        ComponentName componentName = new ComponentName(this, IPLoggerJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)  // Ensure Wi-Fi connection
                .setPersisted(true)  // Persist job across device reboots
                .setPeriodic(24 * 60 * 60 * 1000)  // Run every 15 minutes (minimum allowed period)
                .build();

        jobScheduler.schedule(jobInfo);
    }

}
