package com.iplogger;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class IPLoggerJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        // Run the job in a background thread if needed
        new Thread(() -> {
            Helper.logIPv4Address(IPLoggerJobService.this);
            jobFinished(params, false);  // Indicate that the job is finished
        }).start();

        return true;  // Indicate job is still running
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;  // No need to reschedule if stopped
    }

}
