import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
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
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View/MyView.fxml").openStream());
        root.getStylesheets().add(getClass().getResource("View/MainStyle.css").toExternalForm());
        primaryStage.setTitle("El Laberinto de Leo");
        primaryStage.getIcons().add(new Image("leo.png"));
        Scene scene = new Scene(root, 1000, 800);

        primaryStage.setScene(scene);
//        String musicFile = "resources/UCL.mp3";
//        Media media = new Media(new File(musicFile).toURI().toString()); //replace /Movies/test.mp3 with your file
//        MediaPlayer player = new MediaPlayer(media);
//        player.setAutoPlay(true);
//        player.play();




        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(viewModel);
        viewModel.addObserver(controller);
        controller.setResizeEvent(scene);

        SetStageCloseEvent(primaryStage, model);
        primaryStage.show();

    }

    private void SetStageCloseEvent(Stage primaryStage, IModel m ) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                    m.exit();
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
