package com.skye.fittracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${RESEND_API_KEY}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendVerificationEmail(String to, String token) {

        String url = "https://api.resend.com/emails";

        String verifyLink =
                "https://fittracker-9sb7.onrender.com/api/users/verify?token=" + token;

        Map<String, Object> request = Map.of(
                "from", "FitTracker <onboarding@resend.dev>",
                "to", new String[]{to},
                "subject", "Verify your email",
                "html", "<h2>Welcome to FitTracker</h2>"
                        + "<p>Click below to verify:</p>"
                        + "<a href=\"" + verifyLink + "\">Verify Email</a>"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        restTemplate.postForEntity(url, entity, String.class);
    }
}