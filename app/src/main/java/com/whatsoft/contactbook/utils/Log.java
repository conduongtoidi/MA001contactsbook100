package com.whatsoft.contactbook.utils;


/**
 * Wrapper class for Log.{d,w,e}
 */
public class Log {
    public static String TAG = "ContactsBook";
    public static boolean isDebug = true;

    public static void d() {
        if (isDebug) {
            StackTraceElement t = Thread.currentThread().getStackTrace()[3];
            android.util.Log.d(
                    TAG,
                    t.getClassName().substring(
                            t.getClassName().lastIndexOf('.') + 1)
                            + "." + t.getMethodName());
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            StackTraceElement t = Thread.currentThread().getStackTrace()[3];
            android.util.Log.d(
                    TAG,
                    t.getClassName().substring(
                            t.getClassName().lastIndexOf('.') + 1)
                            + "." + t.getMethodName() + " " + msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (isDebug)
            android.util.Log.d(TAG, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            android.util.Log.d(tag, msg);
    }

    public static void w(Throwable tr) {
        if (isDebug && tr != null)
            android.util.Log.w(TAG, tr.getLocalizedMessage(), tr);
    }

    public static void w(String msg) {
        if (isDebug)
            android.util.Log.w(TAG, msg);
    }

    public static void w(String msg, Throwable tr) {
        if (isDebug)
            android.util.Log.w(TAG, msg, tr);
    }

    public static void e(Throwable tr) {
        if (isDebug && tr != null)
            android.util.Log.e(TAG, tr.getLocalizedMessage(), tr);
    }

    public static void e(String msg) {
        if (isDebug)
            android.util.Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable tr) {
        if (isDebug)
            android.util.Log.e(TAG, msg, tr);
    }

    public static void i(String logtag, String string) {
        if (isDebug)
            android.util.Log.e(logtag, string);
    }

    public static void i(String msg) {
        if (isDebug)
            android.util.Log.i(TAG, msg);
    }
}