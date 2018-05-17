package com.coderzheaven.multiplethread;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    private Handler handler;
    TextView tvStatus;
    int curCount = 0;
    ProgressBar progressBar;
    String url1 = "http://cdn3.pcadvisor.co.uk/cmsdata/features/3420161/Android_800_thumb800.jpg";
    String url2 = "http://cdn2.ubergizmo.com/wp-content/uploads/2015/05/android-logo.jpg";
    float totalCount = 200F;
    private RecyclerView mRecycler;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvDownloadCount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecycler = (RecyclerView) findViewById(R.id.images);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 4);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter = new ImageAdapter());

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        for (int i = 0; i < totalCount; i++) {
            String imageUrl = url1;
            executor.execute(new LongThread(i, imageUrl, new Handler(this), this));
        }

        tvStatus.setText("Starting Download...");
    }

    @Override
    public boolean handleMessage(Message msg) {
        curCount++;
        float per = (curCount / totalCount) * 100;
        progressBar.setProgress((int) per);
        if (per < 100)
            tvStatus.setText("Downloaded [" + curCount + "/" + (int)totalCount + "]");
        else
            tvStatus.setText("All images downloaded.");

        return true;
    }


    public void addImage(Bitmap bitmap) {
        mAdapter.add(bitmap); //still on BG thread.
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
