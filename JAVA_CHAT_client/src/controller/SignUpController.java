/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DTO.LoginDto;
import DTO.UserImage;
import connectorClasses.ScenesConnector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.UserImageInt;
import java_chat_interfaces.Users;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import validation.RegisterValidation;

public class SignUpController implements Initializable {

    ScenesConnector connector = new ScenesConnector();
    private double xPos = 0;
    private double yPos = 0;
    public int id = -1;
    RegisterValidation check;

    @FXML
    Button register;
    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField email;
    @FXML
    TextField password;
    @FXML
    RadioButton male;
    @FXML
    RadioButton female;
    @FXML
    ComboBox<String> country;
    ObservableList<String> countryName = FXCollections.observableArrayList("Egyept", "Palastien", "Syria", "Sudan");
    @FXML
    ImageView minimize;
    @FXML
    ImageView close;

    String gender;
    final ToggleGroup group = new ToggleGroup();

    @FXML
    public void draggAction(MouseEvent event) {

        Stage stage1 = (Stage) close.getScene().getWindow();

        stage1.setX(event.getScreenX() - xPos);
        stage1.setY(event.getSceneY() - yPos);

    }

    @FXML
    public void onMouseClicked(MouseEvent event) {

        xPos = event.getSceneX();
        yPos = event.getSceneY();

    }

    @FXML

    public void confirmRegister(ActionEvent event) {
        Stage stage = connector.getStage();
        if (event.getSource().equals(register)) {
            check = new RegisterValidation();
            if (!check.validateText(firstName.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Name : name must be started with character");
                alert.showAndWait();
            } else if (!check.validateText(lastName.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Name : name must be started with character");
                alert.showAndWait();
            } else if (!check.validatePassword(password.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Password : name must be contain at least one capital letter chatacter,number and special character ");
                alert.showAndWait();
            } else if (!check.validateEmail(email.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Email : name must be contained @ ");
                alert.showAndWait();
            } else {
                Users user = new pojo.Users();
                user.setFirst_name(firstName.getText().toString());
                user.setLast_name(lastName.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.setCountry(country.getPromptText().toString());
//                if (male.isPressed()) {
//                    user.setGender("male");
//                } else {
//                    user.setGender("female");
//                }
                user.setGender(gender);
                Thread th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            id = connector.getChRef().registerUserToDB(user);
                            System.out.println(id);
                            if (id > 0) {

                    try {

                        Parent root = FXMLLoader.load(getClass().getResource("/views/LogIn.fxml"));

                        Scene scene = new Scene(root);
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {

                                stage.setScene(scene);
                            }
                        });

                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
//                            UserImageInt image1=new UserImage();
//                            image1.setUserId(id);
//                            image1.setUserImage(new File(getClass().getClassLoader().getResource("images/default.png").toString()));
//                            connector.getChRef().setImageToDataBase(image1);

                        } catch (RemoteException ex) {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                th.start();

                

            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        register.setOnAction(this::confirmRegister);

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                minimize.setImage(new Image(getClass().getResourceAsStream("/images/minimize.png")));
                close.setImage(new Image(getClass().getResourceAsStream("/images/close_1.png")));

            }
        });

        close.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                Platform.exit();

            }
        });
        minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ((Stage) ((ImageView) event.getSource()).getScene().getWindow()).setIconified(true);

            }
        });
        country.setItems(countryName);

        female.setToggleGroup(group);
        male.setToggleGroup(group);
        female.setUserData("female");
        male.setUserData("male");
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    gender = group.getSelectedToggle().getUserData().toString();
                }
            }
        });
    }

}
