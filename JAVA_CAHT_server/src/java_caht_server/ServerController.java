package java_caht_server;

import Controllers.LoginController;
import Controllers.SignUpControllerFromAdmin;
import Controllers.StatisticsController;
import connectorClasses.ScenesConnector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import server.ChatServerImpl;

public class ServerController implements Initializable {

    File file;
    @FXML
    private TextField txtField;

    @FXML
    private Button onlineB;
    @FXML
    private Button awayB;
    @FXML
    private Button busyB;
    @FXML
    private Button startB;
    @FXML
    private Button stopB;
    @FXML
    private Button statusB;
    @FXML
    private Button oflineB;
    @FXML
    public Label adminName;
    @FXML
    public Label serverstate;
    @FXML
    private ListView userList;
    @FXML
    public ImageView adminImage;
    @FXML
    ImageView userImg;
    @FXML
    private Label emailL;
    @FXML
    private Label nameL;
    @FXML
    private Label genderL;
    @FXML
    private Label statusL;
    @FXML
    private Label countryL;
    @FXML
    private Label close;
    @FXML
    private Label minimize;
    ChatServerImpl obj;
    Registry reg;

    ServerDAO2 sd = new ServerDAO2();
    ScenesConnector connector = JavaProg.getConnector();

//    public static  int adminid;
    public void setAdminName(Label adminName) {
        this.adminName = adminName;
    }

    public void setAdminImage(ImageView adminImage) {
        this.adminImage = adminImage;
    }

    public void setEmailLText(String s) {
        emailL.setText(s);
    }

    public void setNameLText(String s) {
        nameL.setText(s);
    }

    public void setGenderLText(String s) {
        genderL.setText(s);
    }

    public void setStateLText(String s) {
        statusL.setText(s);
    }

    public void setCountryLText(String s) {
        countryL.setText(s);
    }
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void drag1(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();

    }

    @FXML
    private void drag2(MouseEvent event) {
        Stage ps = (Stage) adminName.getScene().getWindow();
        ps.setX(event.getScreenX() - xOffset);
        ps.setY(event.getScreenY() - yOffset);

    }

    public void onlineHandel() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ObservableList<UsersServer> children = FXCollections.observableList(sd.getOnline());
                userList.setItems(children);
            }
        });

    }

    public void oflineHandel() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ObservableList<UsersServer> children = FXCollections.observableList(sd.getOfline());
                userList.setItems(children);
            }
        });
    }

    public void awayHandel() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ObservableList<UsersServer> children = FXCollections.observableList(sd.getAway());
                userList.setItems(children);
            }
        });

    }

    public void busy() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                ObservableList<UsersServer> children = FXCollections.observableList(sd.getBusy());
                userList.setItems(children);
            }
        });
    }

    @FXML
    public void startServer(ActionEvent event) {

        try {

            reg.rebind("ChatService", obj);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Server started ! ");
            alert.showAndWait();
            serverstate.setText("started");
            serverstate.setStyle("-fx-background-color:#00cc66;");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void stopServer(ActionEvent event) {

        try {
            
//            Registry reg = LocateRegistry.getRegistry(11000);
            reg.unbind("ChatService");
            serverstate.setText("stoped");
            serverstate.setStyle("-fx-background-color:#ff0000;");
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void txtFieldAction(ActionEvent event) {

        System.out.println(txtField.getText());
        try {
            obj.sendMessageToAllUsers(txtField.getText().toString());
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtField.clear();
    }

    @FXML
    public void closeAction(MouseEvent e) {
        Platform.exit();
    }

    @FXML
    public void minAction(MouseEvent event) {
        ((Stage) ((Label) event.getSource()).getScene().getWindow()).setIconified(true);

    }

    @FXML
    private void registerFromAdmin() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {

                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setBuilderFactory(new JavaFXBuilderFactory());
                    loader.setLocation(ServerController.class.getResource("/Views/SignUpFromAdmin.fxml"));
                    InputStream in = ServerController.class.getResourceAsStream("/Views/SignUpFromAdmin.fxml");

                    root = loader.load(in);

                    SignUpControllerFromAdmin notifyCtrl = loader.getController();

                    Stage stage = new Stage();

                    stage.initStyle(StageStyle.UNDECORATED);

                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    @FXML
    public void statusBAction() {
        Stage stage = connector.getStage();
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {

                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setBuilderFactory(new JavaFXBuilderFactory());
                    loader.setLocation(ServerController.class.getResource("/Views/statisticsChart.fxml"));
                    InputStream in = ServerController.class.getResourceAsStream("/Views/statisticsChart.fxml");

                    root = loader.load(in);

                    StatisticsController notifyCtrl = loader.getController();

                    Stage stage = new Stage();

                    stage.initStyle(StageStyle.UNDECORATED);

                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            obj = new ChatServerImpl();
            reg = LocateRegistry.createRegistry(11000);

        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        userImg.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));

        userImg.setFitHeight(100);

        userImg.setFitWidth(100);
        Rectangle clip = new Rectangle(
                userImg.getFitWidth(), userImg.getFitHeight()
        );
        clip.setArcWidth(100);
        clip.setArcHeight(100);
        userImg.setClip(clip);

        if (LoginController.getAdmin().getFis() != null) {
            adminImage.setImage(new Image(LoginController.getAdmin().getFis().toURI().toString(), 50, 50, true, true));
        } else {
            adminImage.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
        }

        adminImage.setFitHeight(80);
        adminImage.setFitWidth(80);
        Rectangle clip1 = new Rectangle(
                adminImage.getFitWidth(), adminImage.getFitHeight()
        );
        clip1.setArcWidth(80);
        clip1.setArcHeight(80);
        adminImage.setClip(clip1);

        userList.setCellFactory(new CellRenderer(this));
        ObservableList<UsersServer> children = FXCollections.observableList(sd.getAllUsers());
        userList.setItems(children);

        adminImage.setOnMouseClicked(new EventHandler<MouseEvent>() {

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
                            adminImage.setImage(image);
//                            LoginController.getAdmin().setFis(file);
                            sd.saveImageToDatabase(file, LoginController.getAdmin().getUser_id());
                        }
                    });

                } catch (IOException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

}
