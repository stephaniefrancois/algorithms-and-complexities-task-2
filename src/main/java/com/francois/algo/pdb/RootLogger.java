package com.francois.algo.pdb;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class RootLogger {
    public  static Logger get() {
        return LogManager.getLogManager().getLogger("root");
    }
}
