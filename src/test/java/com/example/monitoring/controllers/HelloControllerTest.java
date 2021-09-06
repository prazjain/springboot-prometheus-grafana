package com.example.monitoring.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.monitoring.SpringbootPrometheusGrafanaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringbootPrometheusGrafanaApplication.class)
public class HelloControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldPassIfStringMatches(){
        final String url = "http://localhost:" + port + "/hello";
        assertEquals("Hello",
            testRestTemplate.getForObject(url, String.class));
    }
}
