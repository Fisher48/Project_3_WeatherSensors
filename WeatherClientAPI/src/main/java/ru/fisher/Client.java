package ru.fisher;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {

    public static void main(String[] args) {
        final String sensorName = "ClientSensor";
        registerSensor(sensorName);
        Random rand = new Random();

        double maxTemperature = 45.0;
        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            addMeasurements(rand.nextDouble() * maxTemperature, rand.nextBoolean(), sensorName);
        }
    }

    public static void registerSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);
        makePostRequests(url, jsonData);
    }

    public static void addMeasurements(double value, boolean isRaining, String sensorName) {
        String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", isRaining);
        jsonData.put("sensorName", Map.of("name", sensorName));

        makePostRequests(url, jsonData);
    }

    public static void makePostRequests(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Измерения внесены");
        } catch (HttpClientErrorException e) {
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
        }
    }

}