package com.example.settingsdemo.helper;

import android.util.Log;

public class DebugLogger {
    public static void log(String debugTag, String component, String msg) {
        Log.d(debugTag, component + ": " + msg);
    }
}
