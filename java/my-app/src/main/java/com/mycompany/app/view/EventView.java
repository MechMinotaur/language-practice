package com.mycompany.app.view;

import java.util.logging.Logger;

public class EventView {

    private final Logger logger;

    public EventView(Logger logger) {
        this.logger = logger;
    }

    public void LogException(String sourceClass, String sourceMethod, Throwable thrown) {
        this.logger.throwing(sourceClass, sourceMethod, thrown);
    }
}
