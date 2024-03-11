package com.example.reteasocialagui;
import com.example.reteasocialagui.Domain.Friendship;
import com.example.reteasocialagui.Domain.MessageAlert;
import com.example.reteasocialagui.Domain.Validation.ValidationException;
import com.example.reteasocialagui.Service.FriendshipService;
import com.example.reteasocialagui.Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.example.reteasocialagui.Domain.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class UserController {
    UserService uService;
    @FXML
    TableView<User> table;
    @FXML
    TableColumn<User, Long> id;
    @FXML
    TableColumn<User, String> nume;
    @FXML
    TableColumn<User, String> oras;
    @FXML
    TableColumn<User, Integer> varsta;
    @FXML
    TextField search;
    FriendshipService serviceFr;
    ObservableList<User> model = FXCollections.observableArrayList();


    public UserController() {
    }
    public void setUserService(UserService service, FriendshipService serviceFr) {
        this.uService = service;
        this.serviceFr = serviceFr;
        this.initModel();
    }
    private void initModel() {
        Iterable<User> messages = this.uService.findAll();
        List<User> users = (List)StreamSupport.stream(messages.spliterator(),false).collect(Collectors.toList());
        this.model.setAll(users);
    }
    public List<User> getUsers(){
        Iterable<User> usr = uService.findAll();
        List<User> users = new ArrayList<>();
        for(User user:usr) users.add(user);
        return users;
    }

    @FXML
    public void initialize() {
        this.id.setCellValueFactory(new PropertyValueFactory("id"));
        this.nume.setCellValueFactory(new PropertyValueFactory("nume"));
        this.oras.setCellValueFactory(new PropertyValueFactory("oras"));
        this.varsta.setCellValueFactory(new PropertyValueFactory("varsta"));
        this.table.setItems(this.model);
        search.textProperty().addListener(o -> handleFilter());
    }

    private void handleFilter() {
        List<User> users = getUsers();
        Predicate<User> p1 = n -> n.getNume().startsWith(search.getText());
        List<User> filtered = users.stream().filter(p1).toList();
        model.setAll(getUsers()
                .stream()
                .filter(p1)
                .collect(Collectors.toList()));
    }


    public void loginUser(ActionEvent event) {
        Parent root;
        User user=(User)table.getSelectionModel().getSelectedItem();
        if(user!=null) {
            try {
                Long idUser = user.getId();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/reteasocialagui/" +
                        "loggedUser.fxml"));
                root = (Parent) fxmlLoader.load();
                LoggedUserController controller = (LoggedUserController) fxmlLoader.getController();
                AnchorPane userLayout = (AnchorPane) root;
                Stage stage = new Stage();
                controller.setService(uService, serviceFr, idUser);
                stage.setTitle("Logged");
                stage.setScene(new Scene(userLayout));

                stage.showAndWait();
                //((Node)(event.getSource())).getScene().getWindow().hide();
                initModel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
