package com.example.reteasocialagui;

import com.example.reteasocialagui.Domain.Validation.FriendshipValidator;
import com.example.reteasocialagui.Domain.Validation.UserValidator;
import com.example.reteasocialagui.Repository.FriendshipDbRepository;
import com.example.reteasocialagui.Repository.UserDbRepository;
import com.example.reteasocialagui.Service.FriendshipService;
import com.example.reteasocialagui.Service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserApplication extends Application {
    UserDbRepository utilizatorRepository;
    FriendshipDbRepository friendshipRepository;
    UserService service;
    FriendshipService fService;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        String username = "postgres";
        String pasword = "Gianina24*";
        String url = "jdbc:postgresql://localhost:5432/ReteaSociala";
        utilizatorRepository = new UserDbRepository(url, username, pasword, new UserValidator());
        this.service = new UserService(utilizatorRepository);
        friendshipRepository = new FriendshipDbRepository(url, username, pasword, new FriendshipValidator());
        this.fService = new FriendshipService(friendshipRepository);
        this.initView(primaryStage);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialagui/user-view.fxml"));
        AnchorPane userLayout = (AnchorPane)fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("MAP Toy Social Network");
        UserController userController = (UserController)fxmlLoader.getController();
        userController.setUserService(this.service, this.fService);
    }
}
