package com.example.reteasocialagui;

import com.example.reteasocialagui.Domain.Friend;
import com.example.reteasocialagui.Domain.Friendship;
import com.example.reteasocialagui.Domain.MessageAlert;
import com.example.reteasocialagui.Domain.User;
import com.example.reteasocialagui.Domain.Validation.ValidationException;
import com.example.reteasocialagui.Service.FriendshipService;
import com.example.reteasocialagui.Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



public class LoggedUserController {
    Long userID;

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    FriendshipService serviceFr;
    UserService serviceUser;
    @FXML
    TableView<Friend> friends;
    @FXML
    TableColumn<Friend, Long> id;
    @FXML
    TableColumn<Friend, User> user;
    @FXML
    TableView<User> table;
    @FXML
    TextField nameUp;
    @FXML
    TextField cityUp;
    @FXML
    TextField ageUp;
    @FXML
    TableColumn<User, Long> id1;
    @FXML
    TableColumn<User, String> nume;
    @FXML
    TableColumn<User, String> oras;
    @FXML
    TableColumn<User, Integer> varsta;
    @FXML
    TableView<Friend> requests;
    @FXML
    TableColumn<Friend, Long> idReq;
    @FXML
    TableColumn<Friend, User> from;
    @FXML
    Button logout;



    ObservableList<Friend> model = FXCollections.observableArrayList();
    ObservableList<User> modelU = FXCollections.observableArrayList();
    ObservableList<Friend> modelR = FXCollections.observableArrayList();

    public LoggedUserController() {
    }
    public void setService(UserService uService, FriendshipService serviceFr, Long idUser) {
        this.serviceFr = serviceFr;
        this.userID = idUser;
        this.serviceUser = uService;
        this.initModel();
    }

    private void initModel() {
        Iterable<Friend> frs = serviceFr.getOfUser(userID);
        List<Friend> ofUser = (List) StreamSupport.stream(frs.spliterator(),false).collect(Collectors.toList());
        Iterable<User> users = serviceUser.findAll();
        List<User> users1 = (List)StreamSupport.stream(users.spliterator(),false).collect(Collectors.toList());
        Iterable<Friend> reqs = serviceFr.findRequests(userID);
        List<Friend> requests =  (List)StreamSupport.stream(reqs.spliterator(),false).collect(Collectors.toList());

        this.model.setAll(ofUser);
        this.modelU.setAll(users1);
        this.modelR.setAll(requests);
    }
    @FXML
    public void initialize() {
        this.id.setCellValueFactory(new PropertyValueFactory("id"));
        this.user.setCellValueFactory(new PropertyValueFactory("friend"));
        this.friends.setItems(this.model);

        this.id1.setCellValueFactory(new PropertyValueFactory("id"));
        this.nume.setCellValueFactory(new PropertyValueFactory("nume"));
        this.oras.setCellValueFactory(new PropertyValueFactory("oras"));
        this.varsta.setCellValueFactory(new PropertyValueFactory("varsta"));
        this.table.setItems(this.modelU);

        this.idReq.setCellValueFactory(new PropertyValueFactory("id"));
        this.from.setCellValueFactory(new PropertyValueFactory("friend"));
        this.requests.setItems(this.modelR);



    }
    public void updateUser(ActionEvent event){
        Optional<User> user=serviceUser.findOne(userID);
        if (user.isPresent()) {
            try {
                String nume;
                if(nameUp.getText().isEmpty()) nume = user.get().getNume();
                else nume = nameUp.getText();
                String oras ;
                if(cityUp.getText().isEmpty()) oras = user.get().getOras();
                else oras = cityUp.getText();
                int varsta;
                if(isNumeric(ageUp.getText())) varsta = Integer.parseInt(ageUp.getText());
                else varsta = user.get().getVarsta();
                serviceUser.updateUser(user.get().getId(), nume, oras, varsta);
                MessageAlert.showSuccesMessage(null, "User actualizat cu succes!");
                initModel();
            }
            catch(ValidationException e){
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public void delFriend(ActionEvent event){
        Friend fr=(Friend)friends.getSelectionModel().getSelectedItem();
        if(fr!=null){
            serviceFr.deleteFriendship(fr.getId());
            initModel();
            MessageAlert.showSuccesMessage(null,"Prieten eliminat cu succes!");
        }
    }

    public void delUser(ActionEvent event) {
            serviceUser.delUser(userID);
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();
            MessageAlert.showSuccesMessage(null, "User eliminat cu succes!");
            initModel();
    }
    public void sendFriendRequest(ActionEvent event){
        User user=(User)table.getSelectionModel().getSelectedItem();
        if(user!=null){
            try {
                serviceFr.sendRequest(userID, user.getId());
                initModel();
            }
            catch (ValidationException e){
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
    }

    public void acceptFriendRequest(ActionEvent event){
        Friend friend = (Friend)requests.getSelectionModel().getSelectedItem();
        if(friend!=null){
            serviceFr.deleteFriendship(friend.getId());
            serviceFr.addFriendship(friend.getFriend().getId(), userID);
            initModel();
        }
    }
    public void declineFriendRequest(ActionEvent event){
        Friend friend = (Friend)requests.getSelectionModel().getSelectedItem();
        if(friend!=null){
            serviceFr.deleteFriendship(friend.getId());
            initModel();
        }
    }
    public void logoutUser(ActionEvent event){
        Stage stage = (Stage) logout.getScene().getWindow();

        stage.close();
    }

}
