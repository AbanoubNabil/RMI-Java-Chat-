/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_chat_client;

import connectorClasses.ScenesConnector;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.ChatServerInt;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
//import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 *
 * @author IROCK
 */
public class JAVA_CHAT_client extends Application {

    private double xPos = 0;
    private double yPos = 0;
    String IPADDRESS_PATTERN = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    String registryIp;
    Optional<String> regIp;

    @Override
    public void start(Stage stage) throws Exception {

        do {
            TextInputDialog dialog = new TextInputDialog("Registry Ip");
            dialog.setTitle("Registry Ip");
            dialog.setHeaderText("Please enter Registry Ip");
            regIp = dialog.showAndWait();
            registryIp = regIp.get();
            if (registryIp.matches(IPADDRESS_PATTERN)) {
                break;
            }
        } while (true);

        ScenesConnector.setRegistryIp(registryIp);

        ScenesConnector connector = new ScenesConnector();
        connector.setStage(stage);

        Parent root = FXMLLoader.load(getClass().getResource("/views/LogIn.fxml"));

        stage.initStyle(StageStyle.UNDECORATED);

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
        stage.setScene(scene);
        stage.setMaxWidth(320);
        stage.setMaxHeight(600);
        stage.setMinWidth(320);
        stage.setMinHeight(650);
        stage.setTitle("Chat");
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        try {
//            Registry reg = LocateRegistry.getRegistry(11000);
//            ChatServerInt chRef = (ChatServerInt) reg.lookup("ChatService");
//            ScenesConnector.setChRef(chRef);
//        } catch (RemoteException ex) {
//            Logger.getLogger(JAVA_CHAT_client.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NotBoundException ex) {
//            Logger.getLogger(JAVA_CHAT_client.class.getName()).log(Level.SEVERE, null, ex);
//
//        }
        launch(args);

    }
}
