package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
//import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java_caht_server.ServerDAO2;
import Validation.RegisterValidation;

public class StatisticsController implements Initializable {

    @FXML
    private Label close;
    @FXML
    private Label minimize;
    @FXML
    private PieChart chart1;
    @FXML
    private PieChart chart2;
    @FXML
    Label cap = new Label("");
    @FXML
    Label cap2 = new Label("");
    @FXML
    Label L1 ;
    @FXML
    Label L2 ;
    @FXML
    Label L3 ;
    @FXML
    Label L4 ;
    @FXML
    Label L5 ;
    @FXML
    Label L6 ;
    @FXML
    Label L7 ;
    
    private double xPos = 0;
    private double yPos = 0;
    int id = -1;
    private double xOffset = 0;
    private double yOffset = 0;
    ServerDAO2 sd = new ServerDAO2();
    
    RegisterValidation check = new RegisterValidation();

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
    private void chart1Action(MouseEvent e) {
        Stage stage = (Stage) close.getScene().getWindow();
        Tooltip container = new Tooltip();
        cap2.setTextFill(Color.DARKORANGE);
        cap2.setStyle("-fx-font: 24 arial;");
        
        for ( PieChart.Data data : chart1.getData()) {

            if (container.isShowing()) {
                container.hide();
            }
            cap2.setText(String.valueOf(data.getPieValue()) + "%");
            container.show(stage, e.getScreenX(), e.getScreenY());
            container.setGraphic(cap2);
        }

    }

    @FXML
    private void chart2Action(MouseEvent e) {
        Stage stage = (Stage) close.getScene().getWindow();
        cap.setTextFill(Color.DARKORANGE);
        cap.setStyle("-fx-font: 24 arial;");
        Tooltip container = new Tooltip();
        
        for ( PieChart.Data data : chart2.getData()) {

            if (container.isShowing()) {
                container.hide();
            }
            cap.setText(String.valueOf(data.getPieValue()) + "%");
            container.show(stage, e.getScreenX(), e.getScreenY());
            container.setGraphic(cap);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Integer all = sd.getAllUsersNumber();
        Integer on = sd.getOnlineUsersNumber();
        Integer of = sd.getOflineUsersNumber();
        Integer aw = sd.getAwayUsersNumber();
        Integer bs = sd.getBusyUsersNumber();
        Integer wom = sd.getWomenUsersNumber();
        Integer men = sd.getMenUsersNumber();
       L1.setText(all.toString());
       L2.setText(on.toString());
       L3.setText(of.toString());
       L4.setText(aw.toString());
       L5.setText(bs.toString());
       L6.setText(wom.toString());
       L7.setText(men.toString());
        close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close.getScene().getWindow().hide();
                //((Node)(event.getSource())).getScene().getWindow().hide();
            }
        });
        minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ((Stage) ((Label) event.getSource()).getScene().getWindow()).setIconified(true);

            }
        });

        ObservableList<PieChart.Data> statuschart
                = FXCollections.observableArrayList(
                        new PieChart.Data("OnLine", on),
                        new PieChart.Data("OfLine", of),
                        new PieChart.Data("Away", aw),
                        new PieChart.Data("Busy", bs));

        chart1.setData(statuschart);

        ObservableList<PieChart.Data> genderchart
                = FXCollections.observableArrayList(
                        new PieChart.Data("Women", wom),
                        new PieChart.Data("Men", men));
        chart2.setData(genderchart);

    }

    
}
