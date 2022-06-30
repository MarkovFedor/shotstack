package com.team.shotstack.service;

import com.team.shotstack.dto.VideoDto;
import com.team.shotstack.entity.Video;
import org.springframework.stereotype.Service;

@Service
public class VideoMapper {
    public Video mapToVideo(VideoDto videoDto) {
        Video video = new Video();
        video.setSourceVideoUrl(videoDto.getSource_video_url());
        video.setSourceAudioUrl(videoDto.getSource_audio_url());
        video.setStatus(videoDto.getStatus());
        video.setAudioTrim(videoDto.getAudioTrim());
        return video;
    }
}
