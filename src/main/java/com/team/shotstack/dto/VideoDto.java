package com.team.shotstack.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoDto {
    private String source_video_url;
    private String source_audio_url;
    private String status;
    private String url;
    private int audioTrim;
    private int videoStart;
    private int videoLength;
}
