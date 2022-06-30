package com.team.shotstack.service;

import com.team.shotstack.dto.VideoDto;
import com.team.shotstack.entity.Video;
import com.team.shotstack.repository.ShotstackRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

@Service
public class ShotstackService {
    private static String apiKey = "mFQ8mEFoet9GQCD9ugZuP91d0gQyVHIv3cIhld19";
    @Autowired
    private ShotstackRepository repository;

    @Autowired
    private VideoMapper videoMapper;
    private HttpClient client;

    public ShotstackService() {
        this.client = HttpClient.newHttpClient();
    }

    public String merge(VideoDto videoDto) throws IOException, InterruptedException, URISyntaxException {
        Video video = videoMapper.mapToVideo(videoDto);
        repository.save(video);
        var body = HttpRequest.BodyPublishers.ofString(configureJSON(videoDto));
        System.out.println(configureJSON(videoDto));
        var request = HttpRequest.newBuilder()
                .uri(new URI("https://api.shotstack.io/stage/render"))
                .header("x-api-key",apiKey)
                .header("accept", "application/json")
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectNode node = new ObjectMapper().readValue(response.body(), ObjectNode.class);
        if(response.statusCode() == 201) {
            video.setMessage(String.valueOf(node.get("message")));
            video.setRender_id(node.get("response").get("id").asText());
            video.setStatus("In progress");
            var newRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.shotstack.io/stage/render/" + video.getRender_id()))
                    .header("x-api-key",apiKey)
                    .header("accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> newResponse = client.send(newRequest, HttpResponse.BodyHandlers.ofString());
            ObjectNode newNode = new ObjectMapper().readValue(newResponse.body(), ObjectNode.class);
            while(!newNode.get("response").get("status").toString().contains((CharSequence) "done")) {
                Thread.sleep(2000);
                newResponse = client.send(newRequest, HttpResponse.BodyHandlers.ofString());
                newNode = new ObjectMapper().readValue(newResponse.body(), ObjectNode.class);
            }
            video.setMessage("OK");
            video.setUrl(newNode.get("response").get("url").asText());

            repository.save(video);

            return video.getUrl();
        } else {
            video.setMessage(node.get("message").asText());
            video.setStatus("Failed");
            return node.get("message").asText();
        }
    }

    private String configureJSON(VideoDto videoDto) {
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode videoAsset = mapper.createObjectNode();
            videoAsset.put("type", "video");
            videoAsset.put("src", videoDto.getSource_video_url());

            ObjectNode audioAsset = mapper.createObjectNode();
            audioAsset.put("type", "audio");
            audioAsset.put("src", videoDto.getSource_audio_url());
            audioAsset.put("trim", videoDto.getAudioTrim());

            ObjectNode videoClip = mapper.createObjectNode();
            videoClip.set("asset", videoAsset);
            videoClip.put("start", videoDto.getVideoStart());
            videoClip.put("length", videoDto.getVideoLength());

            ObjectNode audioClip = mapper.createObjectNode();
            audioClip.set("asset", audioAsset);
            audioClip.put("start", 0);
            audioClip.put("length", videoDto.getVideoLength());

            ArrayNode clipsNode = mapper.createArrayNode();
            clipsNode.addAll(Arrays.asList(videoClip, audioClip));
            ArrayNode tracksNode = mapper.createArrayNode();
            tracksNode.addAll(Arrays.asList(clipsNode));

            ObjectNode clipsNotArrayNode = mapper.createObjectNode();
            ObjectNode tracks = mapper.createObjectNode();
            tracks.set("clips", clipsNode);

            ArrayNode someNode = mapper.createArrayNode();
            someNode.addAll(Arrays.asList(tracks));
            ObjectNode simpleNode = mapper.createObjectNode();
            simpleNode.set("tracks", someNode);

            clipsNotArrayNode.set("tracks", someNode);
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("format", "mp4");
            outputNode.put("resolution", "1080");
            ObjectNode requestNode = mapper.createObjectNode();

            requestNode.set("timeline", clipsNotArrayNode);
            requestNode.set("output", outputNode);

            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
