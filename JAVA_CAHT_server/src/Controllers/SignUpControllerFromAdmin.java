package Controllers;

import connectorClasses.ScenesConnector;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java_chat_interfaces.Users;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java_caht_server.JavaProg;
import java_caht_server.ServerDAO2;
import java_caht_server.UsersServer;
import Validation.RegisterValidation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class SignUpControllerFromAdmin implements Initializable {

    ScenesConnector connector = JavaProg.getConnector();
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
    Label minimize;
    @FXML
    Label close;
    String gender;
    final ToggleGroup group = new ToggleGroup();

    private double xOffset = 0;
    private double yOffset = 0;
    String address;
    Stage stage = connector.getStage();

    @FXML
    private void drag1(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();

    }

    @FXML
    private void drag2(MouseEvent event) {
        Stage ps = (Stage) password.getScene().getWindow();
        ps.setX(event.getScreenX() - xOffset);
        ps.setY(event.getScreenY() - yOffset);

    }

    @FXML
    public void confirmRegister(ActionEvent event) {
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
                ServerDAO2 sd = new ServerDAO2();
//                String path =;
                System.out.println(country.getValue());
                UsersServer user = new UsersServer(firstName.getText(), lastName.getText(), country.getValue(), gender, email.getText(), password.getText(), null);
//   
//                if (male.isFocused()) {
//                    System.out.println("in male");
//                    female.setSelected(false);
//                    user.setGender("male");
//                }
//                if (female.isFocused()) {
//                    System.out.println("in female");
//                    male.setSelected(false);
//                    user.setGender("female");
//                }

                sd.insertUser(user);

                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Views/ServerGui.fxml"));
                    Scene scene = new Scene(root);
                    Platform.runLater(() -> {
                        stage.setScene(scene);
                    });

                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public void initialize(URL loc, ResourceBundle resources) {

        close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close.getScene().getWindow().hide();
            }
        });
        minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage) ((Label) event.getSource()).getScene().getWindow()).setIconified(true);

            }
        });
//        ObservableList<String> countryList = FXCollections.observableArrayList("Egypt", "spain", "france", "canada");
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
