package com.team.shotstack.controller;

import com.team.shotstack.dto.VideoDto;
import com.team.shotstack.service.ShotstackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    private ShotstackService service;

    @PostMapping("/merge")
    public ResponseEntity mergeVideoAndAudio(@RequestBody VideoDto videoDto) {
        String response;
        try {
            System.out.println(videoDto);
            response = service.merge(videoDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(response == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @PostMapping("/testing")
    public void testingService(@RequestBody VideoDto videoDto) {
        System.out.println(videoDto);
    }
}
