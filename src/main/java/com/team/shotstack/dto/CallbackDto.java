package com.team.shotstack.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CallbackDto {
    private String type;
    private String action;
    private String id;
    private String owner;
    private String status;
    private String url;
    private String error;
    private String completed;
}
