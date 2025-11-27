/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.app.view;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author tb
 */
public class EventViewTest {

    private EventView eventView;

    // It's rebuilt each time Setup is called.
    @SuppressWarnings("NonConstantLogger")
    private Logger logger;

    // This is part of JUnit.
    @BeforeEach
    @SuppressWarnings("unused")
    void Setup() {
        this.logger = Mockito.mock(Logger.class);
        this.eventView = new EventView(this.logger);
    }

    @Test
    void TestLogExceptionCallsLoggerThrowing() {
        var sourceClass = "sourceClass";
        var sourceMethod = "sourceMethod";
        var throwable = new Throwable();

        this.eventView.LogException(sourceClass, sourceMethod, throwable);

        verify(this.logger, times(1)).throwing(sourceClass, sourceMethod, throwable);
    }

    @Test
    void TestLogInfoCallsLoggerInfo() {
        var info = "Hello world";

        this.eventView.LogInfo(info);

        verify(this.logger, times(1)).info(info);
    }

    @Test
    void TestLogWarning() {
        var warning = "Hello warning";

        this.eventView.LogWarning(warning);

        verify(this.logger, times(1)).warning(warning);
    }

}
