package com.example.chatti.controller;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class YouTubeController {

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final String APPLICATION_NAME = "SpringBoot-Chat-App";

    @GetMapping("api/youtube/search")
    public ResponseEntity<List<Map<String, String>>> searchVideos(@RequestParam String query) {
        // Check if the API key is properly set in application.properties
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_YOUTUBE_API_KEY_HERE")) {
            System.err.println("YouTube API key is not configured.");
            // Return an error to the client
            return ResponseEntity.status(500).body(null);
        }

        List<Map<String, String>> videoList = new ArrayList<>();
        try {
            // Ensure correct capitalization: YouTube
            YouTube youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            YouTube.Search.List search = youtube.search().list(Arrays.asList("id", "snippet"));

            // Set the parameters for the search request
            search.setKey(apiKey);
            search.setQ(query);
            search.setType(Arrays.asList("video"));
            search.setMaxResults(10L);

            // Execute the search and get the response
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            // Process the search results
            if (searchResultList != null) {
                for (SearchResult searchResult : searchResultList) {
                    Map<String, String> videoData = new HashMap<>();
                    videoData.put("videoId", searchResult.getId().getVideoId());
                    videoData.put("title", searchResult.getSnippet().getTitle());
                    videoData.put("thumbnail", searchResult.getSnippet().getThumbnails().getDefault().getUrl());
                    videoList.add(videoData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
        return ResponseEntity.ok(videoList);
    }
}
