package com.ManiCode.fullstackbackend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TimeController {

    private static final String WORLD_TIME_API_URL = "http://worldtimeapi.org/api/timezone/";

    @GetMapping("/time")
    public ResponseEntity<Object> getTime(@RequestParam("timezone") String timezone) {
        if (!isValidTimeZone(timezone)) {
            return new ResponseEntity<>("Invalid timezone", HttpStatus.BAD_REQUEST);
        }
        if (!isUSTimeZone(timezone)) {
            return new ResponseEntity<>("Not a US timezone", HttpStatus.BAD_REQUEST);
        }
        String url = WORLD_TIME_API_URL + timezone;
        try {
            RestTemplate restTemplate = new RestTemplate();
               Map<String, String> jsonObject = (Map<String, String>)restTemplate.getForObject(url, Object.class);
                JSONObject data = new JSONObject();
                if(!ObjectUtils.isEmpty(jsonObject)) {
                    data.put("abbreviation", jsonObject.get("abbreviation"));
                    data.put("timezone", jsonObject.get("timezone"));
                    data.put("datetime", jsonObject.get("datetime"));
                }
                return new ResponseEntity<>(data.toMap(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isValidTimeZone(String timezone) {
        return Arrays.asList(TimeZone.getAvailableIDs()).contains(timezone);
    }

    public boolean isUSTimeZone(String timezone) {
        List<String> usTimeZones = Arrays.asList(TimeZone.getAvailableIDs()).stream()
                .filter(id -> id.startsWith("America/")).collect(Collectors.toList());
        return usTimeZones.contains(timezone);
    }
}
