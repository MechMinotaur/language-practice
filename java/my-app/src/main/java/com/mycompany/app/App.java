package com.mycompany.app;

import com.mycompany.app.controller.UserController;
import com.mycompany.app.model.UserModel;
import com.mycompany.app.view.UserView;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        var userModel = new UserModel();
        var userView = new UserView();
        var UserController = new UserController();
        System.out.println("Hello from App!");
    }
}
