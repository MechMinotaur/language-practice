package com.mycompany.app.view;

import java.util.logging.Logger;

public class EventView {

    @SuppressWarnings("NonConstantLogger")
    private final Logger logger;

    public EventView(Logger logger) {
        this.logger = logger;
    }

    public void LogException(String sourceClass, String sourceMethod, Throwable thrown) {
        this.logger.throwing(sourceClass, sourceMethod, thrown);
    }

    public void LogInfo(String information) {
        this.logger.info(information);
    }

    public void LogWarning(String warning) {
        this.logger.warning(warning);
    }
}
