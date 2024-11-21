package org.dci.theratrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatabaseBackup {

  private static final String BACKUP_DIR = "/home/dci-student/Documents/projects/thera-track"; //Path for backup
  private static final String DB_NAME = "theratrack_db";
  private static final String USER = "postgres";
  private static final String HOST = "localhost";
  private static final int PORT = 5432;
  private static final String PGPASSWORD = "Dci1234!"; // Replace with the actual password for PostgreSQL user


  public static void main(String[] args) {
    // Create a scheduled executor that will run at fixed intervals (e.g., daily)
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
        1); // one thread in the pool.
    scheduler.scheduleAtFixedRate(DatabaseBackup::performBackup, 0, 24,
        TimeUnit.HOURS); //every 24 hours saving
  }

  // Backup task
  private static void performBackup() {
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String backupFilePath = BACKUP_DIR + "/" + DB_NAME + "_" + date + ".backup";

    // Build the pg_dump command
    String[] command = {
        "pg_dump",
        "-h", HOST,
        "-p", String.valueOf(PORT),
        "-U", USER,
        "-F", "c",  // Format: custom archive format
        "-b",        // Include large objects
        "-v",        // Verbose mode
        "-f", backupFilePath, // Output file path
        DB_NAME
    };

    executeBackup(command);
  }

  // Execute the backup process
  public static void executeBackup(String[] command) {
    try {
      // Set the environment variable for the PostgreSQL password (important for non-interactive mode)
      ProcessBuilder processBuilder = new ProcessBuilder(command);
      processBuilder.environment().put("PGPASSWORD", PGPASSWORD);

      // Start the process
      Process process = processBuilder.start();

      // Wait for the process to complete and check for errors
      int exitCode = process.waitFor();

      if (exitCode == 0) {
        System.out.println("Database backup completed successfully.");
      } else {
        System.err.println("Error during backup. Exit code: " + exitCode);
        printError(process);
      }

    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  // Print the error stream if there is any error
  private static void printError(Process process) throws IOException {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(process.getErrorStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        System.err.println(line);
      }
    }
  }
}
