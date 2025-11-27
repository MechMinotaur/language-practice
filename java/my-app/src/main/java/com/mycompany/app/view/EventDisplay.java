package com.mycompany.app.view;

public interface EventDisplay {

    void LogException(String sourceClass, String sourceMethod, Throwable thrown);

    void LogInfo(String information);

    void LogWarning(String warning);
}
