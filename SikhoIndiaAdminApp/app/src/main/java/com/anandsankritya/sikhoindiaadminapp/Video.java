package com.anandsankritya.sikhoindiaadminapp;

public class Video {
    private String username;
    private String videoId;
    private String category;
    private String playlistTitle;
    private String videoTitle;

    public Video() {
    }

    public Video(String username, String videoId, String category, String playlistTitle, String videoTitle) {
        this.username = username;
        this.videoId = videoId;
        this.category = category;
        this.playlistTitle = playlistTitle;
        this.videoTitle = videoTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
}
