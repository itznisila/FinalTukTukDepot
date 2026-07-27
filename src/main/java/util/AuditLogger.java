package util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    private static final String LOG_FILE = "audit_log.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String action, String itemCode, int quantity) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String entry = timestamp + ", " + action + ", " + itemCode + ", " + quantity;

        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(entry + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Failed to write audit log: " + e.getMessage());
        }
    }
}
