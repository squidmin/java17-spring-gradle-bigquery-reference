package org.squidmin.java.spring.gradle.bigquery.logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger {

    public static short HORIZONTAL_LINE_WIDTH = 50;

    public enum LogType {INFO, DEBUG, ERROR, CYAN}

    public static void log(String str, LogType logType) {
        if (logType == LogType.INFO) {
            str = LogFont.boldGreen(str);
            log.info(str);
        } else if (logType == LogType.DEBUG) {
            str = LogFont.bold(str);
            log.debug(str);
        } else if (logType == LogType.ERROR) {
            log.error(str);
        } else if (logType == LogType.CYAN) {
            str = LogFont.boldCyan(str);
            log.info(str);
        } else {
            log.error("Error: Logger: Invalid LogType.");
        }
    }

    public static void echoHorizontalLine(LogType logType) {
        log("-".repeat(HORIZONTAL_LINE_WIDTH), logType);
    }

}
