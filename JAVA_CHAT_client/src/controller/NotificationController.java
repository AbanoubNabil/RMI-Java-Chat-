/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DTO.FrindsListView;
import connectorClasses.ScenesConnector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.ChatServerInt;
import java_chat_interfaces.FriendListViewInt;
import java_chat_interfaces.StatusValues;
import java_chat_interfaces.UserImageInt;
import java_chat_interfaces.Users;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author IROCK
 */
public class NotificationController implements Initializable {

    private int requestSenderId;

    public int getRequestSenderId() {
        return requestSenderId;
    }

    public void setRequestSenderId(int requestSenderId) {
        this.requestSenderId = requestSenderId;
    }

    @FXML
    ImageView senderImage;
    @FXML
    Label senderNameLable;
    @FXML
    Button AcceptBtn;
    @FXML
    Button CancelBtn;

    ChatServerInt server;
    Users userData;
    UserImageInt userImage;
    InputStream stmImg;
    Image img;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        server = ScenesConnector.getChRef();
        try {
            userData = server.getUserData(ScenesConnector.getSenderRequestId());
            userImage = server.getImageToDataBase(ScenesConnector.getSenderRequestId());
            System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj  " + ScenesConnector.getSenderRequestId());

            if (userImage.getUserImage() != null) {

                stmImg = new FileInputStream(userImage.getUserImage());
                img = new Image(stmImg);
                senderImage.setImage(img);
                senderImage.setFitHeight(80);
                senderImage.setImage(img);

                senderImage.setFitWidth(80);
                Rectangle clip = new Rectangle(
                        senderImage.getFitWidth(), senderImage.getFitHeight()
                );
                clip.setArcWidth(80);
                clip.setArcHeight(80);
                senderImage.setClip(clip);

            }

            senderNameLable.setText(userData.getFirst_name() + " " + userData.getLast_name());
        } catch (RemoteException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        AcceptBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                try {
//                    File file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\" + userData.getEmail() + ".xml");
//                    file.createNewFile();
                    server.addFriend(ScenesConnector.getUserId(), requestSenderId);

                    FriendListViewInt myData = new FrindsListView();
                    myData.setFrindId(ScenesConnector.getUserId());

                    UserImageInt image = server.getImageToDataBase(ScenesConnector.getUserId());

                    myData.setFrindImage(image.getUserImage());
                    myData.setName(ScenesConnector.getUserName());
                    myData.setStatus(StatusValues.ONLINE);
                    server.addMeToHisFriendList(myData, requestSenderId);

                    FriendListViewInt FriendData = new FrindsListView();
                    FriendData.setFrindId(requestSenderId);
                    FriendData.setFrindImage(userImage.getUserImage());
                    FriendData.setName(userData.getFirst_name() + " " + userData.getLast_name());
                    FriendData.setStatus(StatusValues.ONLINE);
                    ScenesConnector.getFriendsList().add(FriendData);

                } catch (RemoteException ex) {
                    Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Sender id in accept button: " + requestSenderId);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

        CancelBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                System.out.println("Sender id in accept button: " + requestSenderId);
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        });

    }

}
