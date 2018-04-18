package controller;

import DTO.FrindsListView;
import connectorClasses.ScenesConnector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.FriendListViewInt;
import java_chat_interfaces.StatusValues;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author IROCK
 */
public class ListCellFactory extends ListCell<FriendListViewInt> {

    ChatViewController chat;
    HBox hBox;

    public ListCellFactory(ChatViewController _chat) {

        this.chat = _chat;

    }

    @Override
    protected void updateItem(FriendListViewInt item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            
             InputStream stmImg=null;
             Image img=null;
                 ImageView imgView=null;
             if(item.getFrindImage()!=null){
                 
                  try {
                stmImg = new FileInputStream(item.getFrindImage());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ListCellFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
                img = new Image(stmImg);

            imgView = new ImageView();
            imgView.setImage(img);
            imgView.setFitHeight(50);
            imgView.setImage(img);

            imgView.setFitWidth(50);
            Rectangle clip = new Rectangle(
                    imgView.getFitWidth(), imgView.getFitHeight()
            );
            clip.setArcWidth(50);
            clip.setArcHeight(50);
            imgView.setClip(clip);
            ScenesConnector.setFriendImage(img);
             }else{
                 
                  

            imgView = new ImageView();
            imgView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
            imgView.setFitHeight(50);
            imgView.setImage(img);

            imgView.setFitWidth(50);
            Rectangle clip = new Rectangle(
                    imgView.getFitWidth(), imgView.getFitHeight()
            );
            clip.setArcWidth(50);
            clip.setArcHeight(50);
            imgView.setClip(clip);
            ScenesConnector.setFriendImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
             
             
             }
           

            Label lbl = new Label(item.getName());
            lbl.setFont(Font.font("Amble CN", FontPosture.ITALIC, 20));

            lbl.setStyle("-fx-background-color:#D6E2F1;-fx-border-radius: 0 0 0 0; -fx-background-radius: 0 10 10 0;-fx-padding: 5 5 5 5; ");
            lbl.setTextFill(Color.BLACK);

            ImageView statusImg = new ImageView();
            if (item.getStatus().equals(StatusValues.ONLINE)) {

                statusImg.setImage(new Image(getClass().getResourceAsStream("/images/online.png")));

            } else if (item.getStatus().equals(StatusValues.BUSY)) {

                statusImg.setImage(new Image(getClass().getResourceAsStream("/images/busy.png")));

            } else {

                statusImg.setImage(new Image(getClass().getResourceAsStream("/images/away.png")));
            }

            statusImg.setFitHeight(15);
            statusImg.setFitWidth(15);
            Rectangle clip1 = new Rectangle(
                    statusImg.getFitWidth(), statusImg.getFitHeight()
            );
            clip1.setArcWidth(15);
            clip1.setArcHeight(15);
            statusImg.setClip(clip1);

            hBox = new HBox(imgView, lbl, statusImg);
            hBox.setStyle("-fx-padding: 0 0 0 5;");
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    ScenesConnector.setFriendId(item.getFrindId());
                    ScenesConnector.setFriendName(item.getName());
                    ScenesConnector.setFriendChossedFlag(true);
                    System.out.println(ScenesConnector.isFriendChossedFlag());
                    chat.frindNameLable.setText(item.getName());
                    chat.myImage.setImage(ScenesConnector.getFriendImage());
                    chat.vBox.getChildren().clear();

                }
            });

            
         
                      setGraphic(hBox);
              
           

        }

    }

}
