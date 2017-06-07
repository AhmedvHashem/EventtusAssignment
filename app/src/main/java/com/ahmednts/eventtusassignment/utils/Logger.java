package com.ahmednts.eventtusassignment.utils;

import android.util.Log;

import com.ahmednts.eventtusassignment.BuildConfig;

/**
 * Created by AhmedNTS on 6/2/2017.
 */
public class Logger {

    private static Logger instance;

    private String TAG;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }

        return instance;
    }

    public Logger withTag(String tag) {
        this.TAG = tag;
        return this;
    }

    public Logger log(String message) {
        if (BuildConfig.DEBUG) {
            Log.println(Log.WARN, TAG, message);
        }
        return this;
    }

    public void withCause(Exception cause) {
        if (BuildConfig.DEBUG) {
            Log.println(Log.WARN, TAG, Log.getStackTraceString(cause));
        }
    }
}