package com.anandsankritya.sikhoindia;

import java.util.ArrayList;

public class VideoItemModel {

    private String videoUrl;
    private String videoTitle;

    public VideoItemModel() {
    }

    public VideoItemModel(String videoUrl, String videoTitle) {
        this.videoUrl = videoUrl;
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
