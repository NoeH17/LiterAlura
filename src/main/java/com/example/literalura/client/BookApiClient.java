package com.example.literalura.client;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class BookApiClient {

    private final HttpClient client;

    public BookApiClient() {
        this.client = HttpClient.newHttpClient();
    }

    public String fetchBooks(String url) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException(
                        "Error fetching books. HTTP Status: " + response.statusCode()
                );
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error connecting to book API", e);
        }
    }
}

