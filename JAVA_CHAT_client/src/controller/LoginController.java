/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DTO.LoginDto;
import connectorClasses.ScenesConnector;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_client.JAVA_CHAT_client;
import java_chat_interfaces.ChatServerInt;
import java_chat_interfaces.Users;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import validation.RegisterValidation;

public class LoginController implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Label register;
    @FXML
    ImageView minimize;
    @FXML
    ImageView close;

    ScenesConnector connector = new ScenesConnector();
    private double xPos = 0;
    private double yPos = 0;
    int id = -1;
    RegisterValidation check = new RegisterValidation();

    private void registerLabelAction() {

        Stage stage = connector.getStage();

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/views/SignUp.fxml"));

            root.setOnMousePressed(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    xPos = event.getSceneX();
                    yPos = event.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xPos);
                    stage.setY(event.getSceneY() - yPos);
                }
            });
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

    @FXML
    private void loginActionButton(ActionEvent event) {
        Stage stage = connector.getStage();

        if (!check.validatePassword(password.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Password : name must be contain at least one capital letter chatacter,number and special character ");
            alert.showAndWait();
        } else if (!check.validateEmail(email.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Email : name must be contained @ ");
            alert.showAndWait();
        } else {

            LoginDto userlogin = new LoginDto();
            userlogin.setEmail(email.getText().toString());
            userlogin.setPassword(password.getText().toString());
            Thread th = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        id = connector.getChRef().login(userlogin);
                        System.out.println(id);
                        if (id > 0) {

                            Users userData=connector.getChRef().getUserData(id);
                            connector.setUserName(userData.getFirst_name()+" "+userData.getLast_name());
                            connector.setUserImage(userData.getImage());
                            
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            th.start();

            if (id > 0) {

                try {
                    connector.setUserId(id);

                    Parent root = FXMLLoader.load(getClass().getResource("/views/ChatView.fxml"));

                    root.setOnMousePressed(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            xPos = event.getSceneX();
                            yPos = event.getSceneY();
                        }
                    });

                    root.setOnMouseDragged(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            stage.setX(event.getScreenX() - xPos);
                            stage.setY(event.getSceneY() - yPos);
                        }
                    });
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

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try {
            Registry reg = LocateRegistry.getRegistry(ScenesConnector.getRegistryIp(), 11000);
            ChatServerInt chRef = (ChatServerInt) reg.lookup("ChatService");
            ScenesConnector.setChRef(chRef);
        } catch (RemoteException ex) {
            Logger.getLogger(JAVA_CHAT_client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(JAVA_CHAT_client.class.getName()).log(Level.SEVERE, null, ex);

        }

        register.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                registerLabelAction();
            }
        });

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

    }

}
