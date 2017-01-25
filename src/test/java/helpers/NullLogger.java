package helpers;

import java.util.logging.*;

public final class NullLogger {
    public final static void configure() {
        Handler handler = new ConsoleHandler();
        Level logLevel = Level.OFF;
        Formatter formatter = new SimpleFormatter();
        Logger logger = Logger.getLogger("root");
        handler.setFormatter(formatter);
        handler.setLevel(logLevel);
        logger.addHandler(handler);
        logger.setLevel(logLevel);
    }
}