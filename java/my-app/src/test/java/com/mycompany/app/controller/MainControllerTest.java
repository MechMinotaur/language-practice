package com.mycompany.app.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mycompany.app.view.EventDisplay;

public class MainControllerTest {
    private MainController mainController;
    private CustomerManager customerManager;
    private EventDisplay eventDisplay;
    private Scanner scanner;

    @BeforeEach
    void Setup() {
        this.customerManager = Mockito.mock(CustomerManager.class);
        this.eventDisplay = Mockito.mock(EventDisplay.class);
        this.scanner = Mockito.mock(Scanner.class);
        this.mainController = new MainController(this.customerManager, this.eventDisplay, this.scanner);
    }

    @Test
    void TestStartMainLoopExitsWhenXEntered() {
        when(this.scanner.nextLine()).thenReturn("X");

        this.mainController.StartMainLoop();

        verify(this.eventDisplay).LogInfo(anyString());
        verify(this.customerManager, never()).displayAllLocalCustomers();
        verify(this.customerManager, never()).updateLocalCustomers();
        verify(this.eventDisplay, never()).LogWarning(anyString());
        verify(this.scanner).close();
    }

    @Test
    void TestStartMainLoopDisplaysCustomersWhenLEntered() {
        when(this.scanner.nextLine())
                .thenReturn("L")
                .thenReturn("X");

        this.mainController.StartMainLoop();

        verify(this.customerManager).displayAllLocalCustomers();
    }

    @Test
    void TestStartMainLoopUpdatesCustomersWhenUEntered() {
        when(this.scanner.nextLine())
                .thenReturn("U")
                .thenReturn("X");

        this.mainController.StartMainLoop();

        verify(this.customerManager).updateLocalCustomers();
    }

    @Test
    void TestStartMainLoopUpdatesCustomersWhenUnknownEntered() {
        when(this.scanner.nextLine())
                .thenReturn("C")
                .thenReturn("X");

        this.mainController.StartMainLoop();

        verify(this.eventDisplay).LogWarning(anyString());
    }

}
