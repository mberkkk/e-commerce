package com.example.UserMicroServiceProject.presentation.web;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// A simple mock controller to fetch AWS region and availability zone from instance metadata
@RestController
public class MetaController {
    @GetMapping("/")
    public String info() {
        try {
            String az = new String(java.net.http.HttpClient.newHttpClient()
                    .send(java.net.http.HttpRequest.newBuilder()
                                    .uri(new java.net.URI("http://169.254.169.254/latest/meta-data/placement/availability-zone"))
                                    .GET().build(),
                            java.net.http.HttpResponse.BodyHandlers.ofString()).body());
            String region = az.substring(0, az.length() - 1);
            return "Region: " + region + ", AZ: " + az;
        } catch (Exception e) {
            return "Metadata not available";
        }
    }
}