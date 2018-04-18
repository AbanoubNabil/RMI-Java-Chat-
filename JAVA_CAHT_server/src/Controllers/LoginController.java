package Controllers;

import Validation.RegisterValidation;
import connectorClasses.ScenesConnector;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java_chat_interfaces.Users;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java_caht_server.JavaProg;
import java_caht_server.ServerController;
import java_caht_server.ServerDAO2;
import java_caht_server.UsersServer;
import javafx.scene.control.Alert;
import javax.print.ServiceUIFactory;

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
  public static UsersServer admin;
 public static UsersServer getAdmin(){
 return admin;
 }
//     LoginController lc=this;
//    public  LoginController getLoginController(){
//    return lc;
//    }
    ScenesConnector connector = JavaProg.getConnector();

    private double xPos = 0;
    private double yPos = 0;
    int id = -1;
    private double xOffset = 0;
    private double yOffset = 0;

    RegisterValidation check = new RegisterValidation();

    Stage stage = connector.getStage();
    @FXML
    private Button loginBtn;

    @FXML
    private void drag1(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();

    }

    @FXML
    private void drag2(MouseEvent event) {
        Stage ps = (Stage) close.getScene().getWindow();
        ps.setX(event.getScreenX() - xOffset);
        ps.setY(event.getScreenY() - yOffset);

    }

    @FXML
    private void registerLabelAction() {

        try {
            Stage stage = connector.getStage();
            Parent root = FXMLLoader.load(getClass().getResource("/Views/SignUp.fxml"));
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
        String pass = password.getText();
        String mail = email.getText();
        Stage stage = connector.getStage();
        if (!check.validatePassword(pass)) {
            System.out.println("password incorrect");
            Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Password : name must be contain at least one capital letter chatacter,number and special character ");
            alert.showAndWait();
        } else if (!check.validateEmail(mail)) {
            System.out.println("email incorrect");
            Alert alert = new Alert(Alert.AlertType.ERROR, "invalid Email : name must be contained @ ");
            alert.showAndWait();
        } else {
             admin = new UsersServer();
            admin.setPassword(pass);
            admin.setEmail(mail);
            
            if (new ServerDAO2().isAdmin(admin)) {
                try {

                    //Parent root = FXMLLoader.load(getClass().getResource("/Views/ServerGui.fxml"));
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ServerGui.fxml"));
                    Parent root = loader.load();
                    ServerController c = loader.getController();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                c.adminName.setText(admin.getFirst_name() + "  " + admin.getLast_name());
                                if (admin.getFis() != null) {
                                    c.adminImage.setImage(new Image(admin.getFis().toURI().toURL().toString(), 50, 50, true, true));
                                }
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    root.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            xPos = event.getSceneX();
                            yPos = event.getSceneY();
                        }
                    });
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
