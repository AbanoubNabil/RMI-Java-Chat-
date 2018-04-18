package java_caht_server;

//import com.messages.User;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

class CellRenderer implements Callback<ListView<UsersServer>, ListCell<UsersServer>> {

    ServerController SC;

    public CellRenderer(ServerController _SC) {
        SC = _SC;
    }

    @Override
    public ListCell<UsersServer> call(ListView<UsersServer> p) {

        ListCell<UsersServer> cell = new ListCell<UsersServer>() {
            Connection con;
            Statement stmt;
            public ResultSet rs = null;

            @Override
            protected void updateItem(UsersServer user, boolean bln) {

                super.updateItem(user, bln);
                PreparedStatement pst;

                setGraphic(null);
                setText(null);
                if (user != null) {
                    try {
                        HBox hBox = new HBox();
                        //Text name = new Text(user.getFirst_name().concat(user.getLast_name()));
                        Text name = new Text(user.getFirst_name() + " " + user.getLast_name());
                        name.setFont(Font.font(null, 20));
                        name.setTextAlignment(TextAlignment.LEFT);
                        ImageView statusImageView = new ImageView();

                        Image statusImage = new Image(getClass().getClassLoader().getResource("images/" + user.getStatus() + ".png").toString(), 16, 16, true, true);
                        //Image statusImage = new Image(getClass().getClassLoader().getResource("images/" + user.getStatus().toString().toLowerCase() + ".png").toString(), 16, 16, true, true);

                        statusImageView.setImage(statusImage);

                        ImageView pictureImageView = new ImageView();
                        if (user.getFis() != null) {
                            pictureImageView.setImage(new Image(user.getFis().toURI().toURL().toString(), 50, 50, true, true));
                        } else {
                            pictureImageView.setImage(new Image(getClass().getResourceAsStream("/images/default.png"), 50, 50, true, true));

                        }

                        pictureImageView.setFitHeight(50);

                        pictureImageView.setFitWidth(50);
                        Rectangle clip = new Rectangle(
                                pictureImageView.getFitWidth(), pictureImageView.getFitHeight()
                        );
                        clip.setArcWidth(50);
                        clip.setArcHeight(50);
                        pictureImageView.setClip(clip);

                        // pictureImageView.setImage(image);
                        hBox.getChildren().addAll(pictureImageView, name, statusImageView);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {

                            @Override
                            public void handle(MouseEvent event) {
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            SC.setCountryLText(user.getCountry());
                                            SC.setEmailLText(user.getEmail());
                                            SC.setGenderLText(user.getGender());
                                            SC.setNameLText(user.getFirst_name() + "   " + user.getLast_name());
                                            SC.setStateLText(user.getStatus());

                                            if (user.getFis() == null) {
                                                SC.userImg.setImage(new Image(getClass().getResourceAsStream("/images/default.png"), 50, 50, true, true));

                                            } else {
                                                SC.userImg.setImage(new Image(user.getFis().toURI().toURL().toString(), 50, 50, true, true));

                                            }
                                        } catch (MalformedURLException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                        setGraphic(hBox);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(CellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };
        return cell;
    }
}
