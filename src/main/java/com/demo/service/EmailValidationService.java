package com.demo.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

@Service
public class EmailValidationService {

    public boolean validateEmail(String email) {
        boolean isValid = false;
        try {
        	HttpRequest request = HttpRequest.newBuilder()
        			.uri(URI.create("https://mailcheck.p.rapidapi.com/?domain="+email))
        			.header("X-RapidAPI-Key", "a1249d7e01msh6102f57eec152f7p18319fjsn3d7f760e4926")
        			.header("X-RapidAPI-Host", "mailcheck.p.rapidapi.com")
        			.method("GET", HttpRequest.BodyPublishers.noBody())
        			.build();
        	HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        	System.out.println(response.body());
        	
        	// JSON processing and setting isValid based on the condition
            String jsonResponse = response.body();
            if (jsonResponse.contains("\"text\":\"Looks okay\"")) {
                isValid = true;
            }
        	
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
