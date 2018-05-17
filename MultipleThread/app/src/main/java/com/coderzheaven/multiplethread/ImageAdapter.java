package com.coderzheaven.multiplethread;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    List<Bitmap> mImages = new ArrayList<>();

    public ImageAdapter(){
    }

    public void add(Bitmap bitmap) {
        synchronized (mImages) {
            mImages.add(0, bitmap);
        }
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.view_image, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Bitmap bitmap = mImages.get(position);

        // Set item views based on the data model
        ImageView imageView = viewHolder.imageView;
        imageView.setImageBitmap(bitmap);
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mImages.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
