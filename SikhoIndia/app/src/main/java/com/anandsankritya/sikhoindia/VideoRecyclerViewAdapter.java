package com.anandsankritya.sikhoindia;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.MyViewHolder>{

    private ArrayList<VideoItemModel> videoList;
    private Context context;
    private String apiKey;
    private int courseId;

    public VideoRecyclerViewAdapter(ArrayList<VideoItemModel> list, Context c, String apiKey, int courseId){
        this.videoList = list;
        this.context = c;
        this.apiKey = apiKey;
        this.courseId = courseId;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.INVISIBLE);
            }
        };

        holder.thumbnailView.initialize(apiKey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videoList.get(holder.getAdapterPosition()).getVideoUrl());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

        holder.videoTitle.setText(videoList.get(holder.getAdapterPosition()).getVideoTitle());

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        YouTubeThumbnailView thumbnailView;
        TextView videoTitle;
        ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            thumbnailView = itemView.findViewById(R.id.thumbnail_view);
            videoTitle = itemView.findViewById(R.id.course_title);
            progressBar = itemView.findViewById(R.id.pb);
        }

        @Override
        public void onClick(View v) {
            Intent toTitleListActivity = new Intent(context, TitleListActivity.class);
            String title = videoList.get(getAdapterPosition()).getVideoTitle();
            toTitleListActivity.putExtra("title", title);
            toTitleListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(toTitleListActivity);
        }
    }
}
