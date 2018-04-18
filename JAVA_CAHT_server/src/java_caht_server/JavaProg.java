package java_caht_server;

//import DAO.ServerDAO;
import connectorClasses.ScenesConnector;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaProg extends Application {
    
    static ScenesConnector connector = new ScenesConnector();
    
    public static ScenesConnector getConnector(){
        return connector;
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        
//        Parent root = loader.load(getClass().getResource("/javaprog/ServerGui.fxml").openStream());
        ServerController controller = loader.getController();
        Parent root = loader.load(getClass().getResource("/Views/LogIn.fxml").openStream());

        Scene scene = new Scene(root);
        //  stage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        connector.setStage(primaryStage);
    }

    public static void main(String[] args) throws FileNotFoundException {


        ServerDAO2 server = new ServerDAO2();
      

        server.DBConnect();

        launch(args);

    }

    
}
