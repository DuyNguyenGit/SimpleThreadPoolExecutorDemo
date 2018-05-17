package com.coderzheaven.multiplethread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;
import java.lang.ref.WeakReference;

public class LongThread implements Runnable {

    int threadNo;
    Handler handler;
    String imageUrl;
    public static final String TAG = "LongThread";

    WeakReference<MainActivity> mActivity = null;

    public LongThread() {
    }

    public LongThread(int threadNo, String imageUrl, Handler handler, MainActivity activity) {
        this.threadNo = threadNo;
        this.handler = handler;
        this.imageUrl = imageUrl;
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void run() {
        Log.i(TAG, "Starting Thread : " + threadNo);
        MainActivity activity = mActivity.get();
        activity.addImage(getBitmap(imageUrl));
        sendMessage(threadNo, "Thread Completed");
        Log.i(TAG, "Thread Completed " + threadNo);
    }


    public void sendMessage(int what, String msg) {
        Message message = handler.obtainMessage(what, msg);
        message.sendToTarget();
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(url).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
            // Do extra processing with the bitmap
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
