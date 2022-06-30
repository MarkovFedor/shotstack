package com.team.shotstack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "video")
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue
    private Long id;
    private String sourceVideoUrl;
    private String sourceAudioUrl;
    private String status;
    private String url;
    private Integer audioTrim;
    private Integer videoStart;
    private Integer videoLength;
    private String render_id;
    private String message;
}
