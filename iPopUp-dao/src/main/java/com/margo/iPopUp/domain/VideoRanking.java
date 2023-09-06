package com.margo.iPopUp.domain;

/**
 * @author margoshaw
 * @date 2023/09/01 20:14
 **/
public class VideoRanking {

    Long ranking;

    Long videoId;

    Long heat;

    public Long getRanking() {
        return ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getHeat() {
        return heat;
    }

    public void setHeat(Long heat) {
        this.heat = heat;
    }
}
