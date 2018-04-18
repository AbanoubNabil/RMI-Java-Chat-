/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DTO.FrindsListView;
import DTO.Message;
import DTO.UserImage;
import client.ClientImpl;
import client.SaveChatJaxB;
import com.healthmarketscience.rmiio.RemoteInputStreamServer;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.sun.deploy.security.ruleset.RunRule;

import connectorClasses.ScenesConnector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_chat_interfaces.ChatServerInt;
import java_chat_interfaces.ClientInt;
import java_chat_interfaces.FriendListViewInt;
import java_chat_interfaces.MessageInt;
import java_chat_interfaces.StatusValues;
import java_chat_interfaces.UserImageInt;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.FilenameUtils;
import validation.RegisterValidation;

public class ChatViewController implements Initializable {

    ChatServerInt chRef;

    @FXML
    ImageView sendFileImageView;

    @FXML
    TextField txtFiled;
    @FXML
    VBox vBox;
    @FXML
    ImageView myImage;
    @FXML
    ImageView minimize;
    @FXML
    ImageView close;
    @FXML
    ImageView userImageView;
    @FXML
    Label usernameLabel;
    @FXML
    ListView<FriendListViewInt> userList;
    @FXML
    Label frindNameLable;

    @FXML
    ImageView searchImg;
    @FXML
    TextField searchTextField;
    @FXML
    ColorPicker color;
    @FXML
    private ComboBox<Integer> fontSize;
    ObservableList<Integer> size = FXCollections.observableArrayList(5, 10, 15, 16, 18, 20, 25, 30, 40);
    @FXML
    private ComboBox<String> fontfamily;
    ObservableList<String> fontType = FXCollections.observableArrayList("Arial Black", "Impact", "arial", "Georgia");
    @FXML
    private ComboBox<String> statusComboBox;
    ObservableList<String> statusType = FXCollections.observableArrayList(StatusValues.ONLINE, StatusValues.AWAY, StatusValues.BUSY);

    int txtSize = 0;
    String font = null;

    private int id;
    private boolean sendFlag = true;
    private boolean reciveFlag = true;
    private boolean fTimeFlag = true;
    private boolean sTimeFlag;
    private boolean found;
    private boolean textColorChoosed;
    private boolean textSizeChoosed;
    private boolean textFontChoosed;
    File file;
    File sentfile;
    int forId;
    private byte[] fileBytes;
    ArrayList<Integer> requestList = new ArrayList<>();

    @FXML
    private void handleTextFiledActionPressed(ActionEvent event) {
        System.out.println("You clicked me!");

        if (!textColorChoosed || !textFontChoosed || !textSizeChoosed) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select color and size and font for your message ");
            alert.showAndWait();

        }

        if (txtFiled.getText().toString().length() > 0 && ScenesConnector.isFriendChossedFlag() && textColorChoosed && textFontChoosed && textSizeChoosed) {
            System.out.println("You clicked me! " + ScenesConnector.isFriendChossedFlag());

            Date date = new Date();
            String dat = String.valueOf(date.toString());

            displayMyMessage(txtFiled.getText());

            MessageInt message = new Message();
            message.setBody(txtFiled.getText().toString());
            message.setColor(toRgbString(color.getValue()));
            message.setSize((fontSize.getValue()));
            message.setDate(dat);
            message.setTo(ScenesConnector.getFriendName());
            message.setFrom(ScenesConnector.getUserName());
            message.setFont(fontfamily.getValue());

            try {
                
                chRef.tellOthers(message, ScenesConnector.getUserId(), ScenesConnector.getFriendId());
            } catch (RemoteException ex) {
                Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
           

            txtFiled.clear();
            System.out.println("message is: "+message.getBody());
             SaveChatJaxB object = new SaveChatJaxB(ScenesConnector.getFriendId(), message ,ScenesConnector.getUserId());
                try {
                    object.write();
                } catch (JAXBException ex) {
                    System.out.println("error in calling xml file class");
                }

        }

    }

    @FXML
    public void setStatusAction(ActionEvent event) {

        System.out.println("Status is; " + statusComboBox.getValue());
        try {
            chRef.changeUserStatus(ScenesConnector.getUserId(), statusComboBox.getValue());
        } catch (RemoteException ex) {
            Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void setColorPic(ActionEvent event) {
        if (font != null && txtSize != 0) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";" + " -fx-font-size:" + fontSize.getValue() + ";");
        } else if (font != null) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";");
        } else if (txtSize != 0) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + " -fx-font-size:" + fontSize.getValue() + ";");

        } else {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";");

        }
        textColorChoosed = true;
    }

    @FXML
    public void setSize(ActionEvent event) {
        if (font != null && txtSize != 0) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";" + " -fx-font-size:" + fontSize.getValue() + ";");
        } else if (font != null) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";");
        } else if (txtSize != 0) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + " -fx-font-size:" + fontSize.getValue() + ";");

        } else {
            txtFiled.setStyle("-fx-font-size: " + fontSize.getValue() + " ;");
        }
        txtSize = fontSize.getValue();
        textSizeChoosed = true;
    }

    @FXML
    public void setFamily(ActionEvent event) {
        if (font != null && txtSize != 0) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";" + " -fx-font-size:" + fontSize.getValue() + ";");
        } else if (font != null) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";");
        } else if (txtSize != 0) {
            txtFiled.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + " -fx-font-size:" + fontSize.getValue() + ";");

        } else {
            txtFiled.setStyle("-fx-font-family: " + fontfamily.getValue() + " ;");
        }
        font = fontfamily.getValue();
        textFontChoosed = true;
    }

    private String toRgbString(Color c) {
        return "rgb("
                + to255Int(c.getRed())
                + "," + to255Int(c.getGreen())
                + "," + to255Int(c.getBlue())
                + ")";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fontSize.setItems(size);
        fontfamily.setItems(fontType);
        statusComboBox.setItems(statusType);

        try {
            chRef = ScenesConnector.getChRef();
            id = chRef.register((ClientInt) new ClientImpl(this));
            ObservableList<FriendListViewInt> obslist = FXCollections.observableArrayList();
            ArrayList<FriendListViewInt> arrList = chRef.getFriendList(ScenesConnector.getUserId());
            obslist.setAll(arrList);

            ScenesConnector.setFriendsList(obslist);

            UserImageInt usrimg = chRef.getImageToDataBase(ScenesConnector.getUserId());

            File imgfile = usrimg.getUserImage();
            if (imgfile != null) {

                InputStream stmImg = new FileInputStream(imgfile);
                Image img = new Image(stmImg);
                userImageView.setImage(img);
                ScenesConnector.setUserImage(img);
            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                userList.setItems(ScenesConnector.getFriendsList());
                userList.setCellFactory(new Callback<ListView<FriendListViewInt>, ListCell<FriendListViewInt>>() {

                    @Override
                    public ListCell<FriendListViewInt> call(ListView<FriendListViewInt> param) {
                        return new ListCellFactory(ChatViewController.this);
                    }
                });

            }
        });

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                searchImg.setStyle("-fx-border-radius: 5 5 5 5; -fx-background-radius: 5 5 5 5; ");
                searchTextField.setStyle("-fx-border-radius: 5 5 5 5; -fx-background-radius: 5 5 5 5; ");
                sendFileImageView.setImage(new Image(getClass().getResourceAsStream("/images/attach.png")));
                sendFileImageView.setStyle("-fx-border-radius: 5 5 5 5; -fx-background-radius: 5 5 5 5; ");

                minimize.setImage(new Image(getClass().getResourceAsStream("/images/minimize.png")));
                close.setImage(new Image(getClass().getResourceAsStream("/images/close_1.png")));
                myImage.setImage(new Image(getClass().getResourceAsStream("/images/images.jpg")));
                usernameLabel.setText(ScenesConnector.getUserName());
                if (ScenesConnector.getUserImage() != null) {
                    userImageView.setImage(ScenesConnector.getUserImage());
                }

            }
        });

        sendFileImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (!ScenesConnector.isFriendChossedFlag()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select friend from the friends list  ");
                    alert.showAndWait();

                } else {
                    Stage stage = new Stage();
                    FileChooser chooser = new FileChooser();
                    file = chooser.showOpenDialog(stage);
                    String str = null;
                    try {
                        str = file.getCanonicalPath();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("file name of file chosser" + str);
                    String ext2 = FilenameUtils.getExtension(str);
                    ScenesConnector.setFileChoosedName(str);
                    ScenesConnector.setFileExtention(ext2);
                    System.out.println(ScenesConnector.getFileChoosedName());
                    System.out.println(ScenesConnector.getFileExtention());

                    try {
                        chRef.requestToSendFile(ScenesConnector.getUserId(), ScenesConnector.getFriendId());
                    } catch (RemoteException ex) {
                        Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });

        searchImg.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                RegisterValidation validation = new RegisterValidation();

                if (validation.validateEmail(searchTextField.getText().toString()) && searchTextField.getText().toString().length() > 0) {

                    try {
                        found = chRef.checkIfFriendIsFounde(searchTextField.getText().toString(), ScenesConnector.getUserId());
                    } catch (RemoteException ex) {
                        Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (!found) {

                        int id = 0;
                        try {
                            id = chRef.addRequest(searchTextField.getText().toString(), ScenesConnector.getUserId());
//                            File file = new File("C:\\Users\\"+ System.getProperty("user.name")+"\\Desktop\\"+searchTextField.getText().toString()+".xml");
//                            file.createNewFile();
                        } catch (RemoteException ex) {
                            Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("request id: " + id);
                        searchTextField.clear();

                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valied email ");
                    alert.showAndWait();
                }

            }
        });

        close.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                try {

                    System.out.println("Status is; " + statusComboBox.getValue());
                    chRef.changeUserStatus(ScenesConnector.getUserId(), StatusValues.BUSY);
                    chRef.unRegister((ClientInt) new ClientImpl(ChatViewController.this));
                } catch (RemoteException ex) {
                    Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Platform.exit();

            }
        });
        minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                ((Stage) ((ImageView) event.getSource()).getScene().getWindow()).setIconified(true);

            }
        });
        userImageView.setFitHeight(60);
        userImageView.setFitWidth(60);
        Rectangle clipuser = new Rectangle(
                userImageView.getFitWidth(), userImageView.getFitHeight()
        );
        clipuser.setArcWidth(60);
        clipuser.setArcHeight(60);
        userImageView.setClip(clipuser);

        userImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                Stage stage = new Stage();
                FileChooser chooser = new FileChooser();
                file = chooser.showOpenDialog(stage);
                chooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
                try {
                    //System.out.println(file);
                    BufferedImage bufferedImage = ImageIO.read(file);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            userImageView.setImage(image);
                        }
                    });

                    Thread th = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            UserImageInt usrimg = new UserImage();
                            usrimg.setUserId(ScenesConnector.getUserId());
                            usrimg.setUserImage(file);
                            try {
                                chRef.setImageToDataBase(usrimg);
                            } catch (RemoteException ex) {
                                Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                    th.start();

                } catch (IOException ex) {
                    Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Thread requestThread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    try {
                        requestList = chRef.retriveRequestList(ScenesConnector.getUserId());
                        if (requestList.size() > 0) {

                            for (int i = 0; i < requestList.size(); i++) {

                                System.out.println("request list memper: " + requestList.get(i));
                                try {
                                    Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
                                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
                                    mediaPlayer.play();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            Parent root;

                                            FXMLLoader loader = new FXMLLoader();
                                            loader.setBuilderFactory(new JavaFXBuilderFactory());
                                            loader.setLocation(ChatViewController.class.getResource("/views/notification.fxml"));
                                            InputStream in = ChatViewController.class.getResourceAsStream("/views/notification.fxml");
                                            ScenesConnector.setSenderRequestId(requestList.get(forId));
                                            root = loader.load(in);

                                            NotificationController notifyCtrl = loader.getController();
                                            System.out.println(notifyCtrl);
                                            notifyCtrl.setRequestSenderId(requestList.get(forId));
                                            System.out.println("fdgfdgg: " + requestList.get(forId));

                                            forId++;

                                            Stage stage = new Stage();
                                            stage.initStyle(StageStyle.UNDECORATED);

                                            stage.setScene(new Scene(root, 542, 170));
                                            stage.show();
                                            // Hide this current window (if this is what you want)
                                            // ((Node) (event.getSource())).getScene().getWindow().hide();
                                        } catch (IOException ex) {
                                            Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });

                            }
                        }
                        Thread.sleep(5000);

                    } catch (IOException ex) {
                        Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }
        );
        requestThread.start();
        System.out.println("Status is; " + statusComboBox.getValue());
        try {
            chRef.changeUserStatus(ScenesConnector.getUserId(), StatusValues.ONLINE);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You can change your image by press on the profile image  ");
        alert.showAndWait();

    }

    public void display(MessageInt msg, int senderId) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                if (ScenesConnector.getFriendId() != senderId) {
                    vBox.getChildren().clear();
                }
                for (int i = 0; i < ScenesConnector.getFriendsList().size(); i++) {

                    if (ScenesConnector.getFriendsList().get(i).getFrindId() == senderId) {
                        String senderName = ScenesConnector.getFriendsList().get(i).getName();
                        ScenesConnector.setFriendName(senderName);
                        frindNameLable.setText(senderName);

                    }
                }
            }
        });

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                if (vBox.getChildren().size() > 0) {

                    HBox b = (HBox) vBox.getChildren().get(vBox.getChildren().size() - 1);

                    if (b.getChildren().get(0) instanceof ImageView) {

                        b.getChildren().remove(0);
                    }

                }

                if (reciveFlag) {

                    Date date = new Date();
                    String d = String.valueOf(date.toString());

                    Label dateLable = new Label(d);
                    dateLable.setStyle("-fx-padding: 5 5 5 5; ");
                    HBox dateHB = new HBox(dateLable);
                    dateHB.setAlignment(Pos.CENTER_LEFT);

                    Label l = new Label(ScenesConnector.getFriendName());
                    l.setStyle("-fx-padding: 10 10 10 10; ");
                    HBox hB = new HBox(l);

                    hB.setAlignment(Pos.CENTER_LEFT);
                    vBox.getChildren().add(dateHB);

                    vBox.getChildren().add(hB);
                    reciveFlag = false;
                    sendFlag = true;
                    fTimeFlag = false;

                }

                ImageView imgView = new ImageView();
                imgView.setImage(ScenesConnector.getFriendImage());
                imgView.setFitHeight(40);
                imgView.setFitWidth(40);
                Rectangle clip = new Rectangle(
                        imgView.getFitWidth(), imgView.getFitHeight()
                );
                clip.setArcWidth(40);
                clip.setArcHeight(40);
                imgView.setClip(clip);

                Label lbl = new Label(msg.getBody());
                lbl.setFont(Font.font(msg.getFont(), FontPosture.ITALIC, msg.getSize()));

                lbl.setStyle("-fx-text-fill: " + msg.getColor() + ";-fx-background-color:#D6E2F1;-fx-border-radius: 0 0 0 0; -fx-background-radius: 0 10 10 0;-fx-padding: 5 5 5 5; ");
                lbl.setTextFill(Color.BLACK);
                HBox hBox = new HBox(imgView, lbl);
                hBox.setStyle("-fx-padding: 0 0 0 15;");

                hBox.setAlignment(Pos.CENTER_LEFT);

                vBox.getChildren().add(hBox);

            }
        });

        System.out.println("message is: "+msg.getBody());
       
        
             SaveChatJaxB object = new SaveChatJaxB(ScenesConnector.getFriendId(), msg , ScenesConnector.getUserId());
                try {
                    object.write();
                } catch (JAXBException ex) {
                    System.out.println("error in calling xml file class");
                }
    }

    public void displayMyMessage(String msg) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                if (sendFlag) {

                    Date date = new Date();
                    String d = String.valueOf(date.toString());
                    Label dateLable = new Label(d);
                    dateLable.setStyle("-fx-padding: 5 5 5 5; ");
                    HBox dateHB = new HBox(dateLable);
                    dateHB.setAlignment(Pos.CENTER_RIGHT);
                    vBox.getChildren().add(dateHB);

                    Label l = new Label(ScenesConnector.getUserName().toString());
                    l.setStyle("-fx-padding: 10 10 10 10; ");
                    HBox hB = new HBox(l);

                    hB.setAlignment(Pos.CENTER_RIGHT);

                    vBox.getChildren().add(hB);
                    sendFlag = false;
                    reciveFlag = true;
                    fTimeFlag = true;
                }
                if (fTimeFlag) {

                    Label lbl = new Label(msg);

                    // lbl.setFont(Font.font("Amble CN", FontWeight.LIGHT, 16));
                    lbl.setStyle("-fx-background-color: #1DA1F2;-fx-border-radius: 0 0 0 0; -fx-background-radius: 10 10 0 10;-fx-padding: 5 5 5 5; ");

                    lbl.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";" + " -fx-font-size:" + fontSize.getValue() + ";-fx-background-color: #1DA1F2;-fx-border-radius: 0 0 0 0; -fx-background-radius: 10 10 0 10;-fx-padding: 5 5 5 5;");

                    //lbl.setTextFill(Color.WHITE);
                    HBox hBox = new HBox(lbl);
                    hBox.setStyle("-fx-padding: 0 0 0 0;");

                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    vBox.getChildren().add(hBox);
                    fTimeFlag = false;
                    sTimeFlag = false;
                    return;

                }
                if (sTimeFlag) {

                    if (vBox.getChildren().size() > 0) {

                        HBox b = (HBox) vBox.getChildren().get(vBox.getChildren().size() - 1);

                        if (b.getChildren().get(0) instanceof Label) {

                            Label l = (Label) b.getChildren().get(0);

                            l.setStyle("-fx-background-color: #1DA1F2;-fx-border-radius: 0 0 0 0; -fx-background-radius: 10 0 0 10;-fx-padding: 5 5 5 5; ");

                            l.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";" + " -fx-font-size:" + fontSize.getValue() + ";-fx-background-color: #1DA1F2;-fx-border-radius: 0 0 0 0; -fx-background-radius: 10 0 0 10;-fx-padding: 5 5 5 5;");

                        }

                    }

                }

                if (!fTimeFlag) {

                    Label lbl = new Label(msg);
                    //lbl.setFont(Font.font("Amble CN", FontWeight.LIGHT, 16));
                    lbl.setStyle("-fx-background-color: #1DA1F2;-fx-border-radius: 0 0 0 0; -fx-background-radius: 10 0 10 10;-fx-padding: 5 5 5 5; ");
                    // lbl.setTextFill(Color.WHITE);
                    lbl.setStyle("-fx-text-fill: " + toRgbString(color.getValue()) + ";" + "-fx-font-family: " + fontfamily.getValue() + ";" + " -fx-font-size:" + fontSize.getValue() + ";-fx-background-color: #1DA1F2;-fx-border-radius: 0 0 0 0; -fx-background-radius: 10 0 10 10;-fx-padding: 5 5 5 5; ");

                    HBox hBox = new HBox(lbl);
                    hBox.setStyle("-fx-padding: 0 0 0 0;");

                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    vBox.getChildren().add(hBox);
                    sTimeFlag = true;

                }

            }
        });

    }

    public void showServerMessage(String message) {

        System.out.println("message is : jkjdajskdas " + message);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
                alert.showAndWait();
            }
        });

    }

    public void showSendFileNotification(int friendId) {

        try {
            Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    Parent root;

                    FXMLLoader loader = new FXMLLoader();
                    loader.setBuilderFactory(new JavaFXBuilderFactory());
                    loader.setLocation(ChatViewController.class.getResource("/views/fileNotification.fxml"));
                    InputStream in = ChatViewController.class.getResourceAsStream("/views/fileNotification.fxml");
                    ScenesConnector.setFileSenderRequestId(friendId);
                    root = loader.load(in);

                    fileNotification notifyCtrl = loader.getController();
                    System.out.println(notifyCtrl);
                    notifyCtrl.setRequestSenderId(friendId);
                    System.out.println("fdgfdgg: " + friendId);

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);

                    stage.setScene(new Scene(root, 542, 170));
                    stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public void uploadFileToServer(String filePath, String extention, int senderId, int reciverId) throws RemoteException {
        System.out.println("insied serve uploadFileToServer method in chatviewController");

        System.out.println("file path is: " + filePath);
        String str = ScenesConnector.getFileChoosedName().replace('\\', '/');

        File sentFile2 = new File(str);
        try {
            if (sentFile2 != null) {
                fileBytes = new byte[(int) sentFile2.length()];
                fileBytes = Files.readAllBytes(sentFile2.toPath());
                ByteArrayInputStream fileStream = new ByteArrayInputStream(fileBytes);
                RemoteInputStreamServer data = new SimpleRemoteInputStream(fileStream);
                ScenesConnector.getChRef().TellOtheFiles(data.export(), ScenesConnector.getFileExtention(), senderId, reciverId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    static InputStream sendFile(String path) throws FileNotFoundException {
        File file = new File(path);

        InputStream in = new FileInputStream(file);

        return in;
    }

}
