package com.anandsankritya.sikhoindia;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
public class TitleListAdapter extends RecyclerView.Adapter<TitleListAdapter.MyViewHolder> {

    private ArrayList<VideoItemModel> videoList;
    private Context context;
    private String apiKey;

    public TitleListAdapter(ArrayList<VideoItemModel> list, Context c, String apiKey) {
        this.videoList = list;
        this.context = c;
        this.apiKey = apiKey;

    }

    @NonNull
    @Override
    public TitleListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, viewGroup, false);
        return new TitleListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TitleListAdapter.MyViewHolder holder, int i) {

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            videoTitle = itemView.findViewById(R.id.course_title);
            thumbnailView = itemView.findViewById(R.id.thumbnail_view);
        }

        @Override
        public void onClick(View v) {

            Intent toVideoPlayerActivity = new Intent(context, VideoPlayerActivity.class);
            toVideoPlayerActivity.putExtra("videoid", videoList.get(getAdapterPosition()).getVideoUrl());
            toVideoPlayerActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(toVideoPlayerActivity);
        }
    }
}
