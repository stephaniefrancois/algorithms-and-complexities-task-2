package com.francois.algo.pdb;

import com.francois.algo.pdb.composition.ServiceFactory;
import com.francois.algo.pdb.core.PdbChainFinder;

import java.io.IOException;
import java.util.logging.*;

public class Main {

    private static Logger log = configureRootLogger();

    public static void Main(String[] args) {

        log.info("PDG ALGO app starting ...");

        ServiceFactory factory = ServiceFactory.getInstance();
        PdbChainFinder finder = factory.createPdbChainFinder();

        // TODO: based on user input pass in correct MATCHER and COMPARATOR!
        // TODO: configure CONSOLE logger to output errors and warnings!
        // TODO: build UI MENU
        // TODO: build COMPARATOR for EC_NUMBER for multiple levels

//        SwingUtilities.invokeLater(() -> {
//            JFrame main = new AppFrame();
//        });

        log.info("PDG ALGO application has been started successfully.");

    }
    private static Logger configureRootLogger() {
        Handler handler = new ConsoleHandler();
        Level logLevel = Level.ALL;
        try {
            handler = new FileHandler("./app.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Formatter formatter = new SimpleFormatter();
        Logger logger = Logger.getLogger("root");
        handler.setFormatter(formatter);
        handler.setLevel(logLevel);
        logger.addHandler(handler);
        logger.setLevel(logLevel);

        return logger;
    }
}
