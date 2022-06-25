package View;
import Model.MyModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.util.Optional;

public class Main extends Application {
//
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
        primaryStage.setTitle("El laberinto de Leo");
        primaryStage.getIcons().add(new Image("leo.png"));
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
