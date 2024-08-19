package com.android.certification.niap.permission.dpctester.test.log;

import androidx.annotation.NonNull;

public class StaticLogger {
    private static Logger logger = new LogcatLogger("Dpm Tester Static Log");

    public static void debug(@NonNull String message) {
        logger.debug(message);
    }

    public static void debug(@NonNull String message, @NonNull Throwable throwable) {
        logger.debug(message,throwable);
    }

    public static  void info(@NonNull String message) {
        logger.info(message);
    }

    public static void info(@NonNull String message, @NonNull Throwable throwable) {
        logger.info(message,throwable);
    }

    public static void error(@NonNull String message) {
        logger.error(message);
    }

    public static void error(@NonNull String message, @NonNull Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void system(@NonNull String message) {
        logger.system(message);
    }

    public void warn(@NonNull String message) {
        logger.warn(message);
    }
}
