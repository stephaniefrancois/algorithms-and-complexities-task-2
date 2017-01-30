package com.francois.algo.pdb;

import com.francois.algo.pdb.app.DashboardUI;
import com.francois.algo.pdb.composition.ServiceFactory;

import java.io.IOException;
import java.util.logging.*;

public class Main {

    private static Logger log = configureRootLogger();

    public static void main(String[] args) {
        log.info("PDB ALGO app starting ...");
        ServiceFactory factory = ServiceFactory.getInstance();
        DashboardUI dashboard = factory.createDashboard(System.in, System.out);
        dashboard.displayMainMenu();
    }
    private static Logger configureRootLogger() {
        Handler handler = new ConsoleHandler();
        Handler consoleHandler = new ConsoleHandler();
        Level logLevel = Level.FINE;
        try {
            handler = new FileHandler("./app.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Formatter formatter = new SimpleFormatter();
        Logger logger = Logger.getLogger("root");
        handler.setFormatter(formatter);
        handler.setLevel(logLevel);
        consoleHandler.setLevel(Level.WARNING);
        logger.addHandler(handler);
        logger.addHandler(consoleHandler);
        logger.setLevel(logLevel);

        return logger;
    }
}
