package app.tfm.automation.reporting;

@SuppressWarnings("null")
public class TestLogManager {

    private static ThreadLocal<StringBuilder> testLog = ThreadLocal.withInitial(StringBuilder::new);

    public static void log(String message) {
        testLog.get().append(message).append("\n");
    }

    public static String getLogs() {
        return testLog.get().toString();
    }

    public static void clearLogs() {
        testLog.remove();
    }
}
