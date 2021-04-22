package com.example.moviesapp;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutor {
    //For Singleton Initialization
    private static final Object lock = new Object();
    private static AppExecutor sInstance;

    //All the three threads one for the main Thread, one for network operation and the other for disk operations.
    private static Executor diskIO;
    private static Executor mainThread;
    private static Executor networkIO;

    private AppExecutor(Executor disk, Executor main, Executor network){
        diskIO = disk;
        networkIO = network;
        mainThread = main;
    }

    //This insures that only one object of the AppExecutors is created for the entire Application
    public static AppExecutor getInstance(){
        if (sInstance==null){
            synchronized (lock){
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),new MainThreadExecutor(),Executors.newFixedThreadPool(4));
            }
        }
        return sInstance;
    }

    public  Executor getDiskIO() {
        return diskIO;
    }

    public  Executor getMainThread() {
        return mainThread;
    }

    public  Executor getNetworkIO() {
        return networkIO;
    }


    //This class creates the MainThreadPool buy implementing the Executor interface.
    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
