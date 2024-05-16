package com.rosedine.rosedine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class DataImportRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String script1Path = "PyScraping/main.py";

        //runPythonScript(script1Path);

        //sendPostRequest();
    }

    private void runPythonScript(String scriptPath) throws Exception {
        System.out.println("Running Python script: " + scriptPath);
        ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Python script " + scriptPath + " failed with exit code " + exitCode);
            System.err.println("Error Output:\n" + errorOutput);
            System.out.println("Standard Output:\n" + output);
            throw new RuntimeException("Python script " + scriptPath + " exited with code " + exitCode);
        } else {
            System.out.println("Python script " + scriptPath + " executed successfully.");
            System.out.println("Standard Output:\n" + output);
        }
    }

    private void sendPostRequest() {
        System.out.println("Sending POST request");
        String url = "http://localhost:8081/api/menus/import";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("Data import status: " + response.getBody());
    }
}
