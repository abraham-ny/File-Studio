/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package filestudio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger implementation with log levels and asynchronous logging
 *
 * @author Abraham Moruri
 */
public class FLogger {

    public enum LogLevel {
        INFO,
        WARN,
        ERROR
    }

    String fileName = "fs-logs.txt";
    private File logFile = new File(Util.home + "\\" + fileName);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public FLogger() {
        //empty constructor
    }

    public FLogger(File logFilem) {
        this.logFile = logFilem;
    }

    public void Log(LogLevel level, String... data) {
        executor.submit(() -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            try (FileWriter fw = new FileWriter(logFile, true)) {
                for (String s : data) {
                    fw.write("[" + timestamp + "] [" + level.name() + "] " + s + "\n");
                }
            } catch (IOException e) {
                System.err.println("Logging failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public File getLogFile() {
        return this.logFile;
    }

    public void shutdown() {
        executor.shutdown();
    }

    public static interface FSLogger {

//        public abstract void log(String str){
//
//        }
    }
}
