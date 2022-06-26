package View;
import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.File;
import java.util.Optional;

public class Main extends Application {
//
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("El Laberinto de Leo");
        primaryStage.getIcons().add(new Image("leo.png"));
        primaryStage.setScene(new Scene(root, 1000, 800));
        String musicFile = "resources/UCL.mp3";
        Media media = new Media(new File(musicFile).toURI().toString()); //replace /Movies/test.mp3 with your file
        MediaPlayer player = new MediaPlayer(media);
        player.setAutoPlay(true);
        player.play();




        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(viewModel);
        viewModel.addObserver(controller);

        SetStageCloseEvent(primaryStage);
        primaryStage.show();

    }

    private void SetStageCloseEvent(Stage primaryStage ) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                    Platform.exit();
                    System.exit(0);
                }
            }
        );
    }
    public static void main(String[] args) {
        launch(args);
    }
}
