package com.rosedine.rosedine.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {
    private static final String LOG_FILE = "C:/Users/khattam/Downloads/debugged.txt";


    public static void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            pw.println(dtf.format(now) + " - " + message);
        } catch (IOException e) {
            System.err.println("Unable to write to log file: " + e.getMessage());
        }
    }


}
