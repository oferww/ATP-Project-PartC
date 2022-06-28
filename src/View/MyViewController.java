package View;

import Server.Configurations;
import Server.ServerStrategySolveSearchProblem;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Observer, IView, Initializable {

    private MyViewModel viewModel;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label lbl_player_row;
    @FXML
    public Label lbl_player_column;
    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();
    private int [][] maze;
    private Maze mazefull;
    String musicFile = "resources/UCL.mp3";
    Media media = new Media(new File(musicFile).toURI().toString()); //replace /Movies/test.mp3 with your file
    MediaPlayer player = new MediaPlayer(media);
    String musicFile1 = "resources/casi goal.mp3";
    Media media1 = new Media(new File(musicFile1).toURI().toString()); //replace /Movies/test.mp3 with your file
    MediaPlayer player1 = new MediaPlayer(media1);
    boolean presssol = false;
    boolean pressgen = false;
    boolean started = false;
    String algo = "";
    String gen = "";
    int threads = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);


    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public String get_update_player_position_row() {
        return update_player_position_row.get();
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public String get_update_player_position_col() {
        return update_player_position_col.get();
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }
    public void generateMaze()
    {
        pressgen = !pressgen;
        presssol =false;
        player.play();
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows,cols);
        viewModel.solveMaze(this.maze);
        started = true;

    }

    public void generatenewMaze(Stage p)
    {

        p.close();
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        presssol =false;
        viewModel.generatenewMaze(rows,cols);
        viewModel.solveMaze(this.maze);
    }

    public void solveMaze()
    {
        viewModel.solveMaze(this.maze);
    }

    public void presssolveMaze()
    {
        if (started) {
            presssol = !presssol;
            viewModel.solveMaze(this.maze);
        }
    }


    public void showAlert(String message)
    {
        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        Button button0= new Button("I want to watch my goal again!");
        button0.setOnAction(e -> playgoal());
        Button button1= new Button("I want to score another goal!");
        button1.setOnAction(e -> generatenewMaze(popupwindow));
        Button button2= new Button("Thank you, I want to go rest now");
        button2.setOnAction(e -> System.exit(0));
        Label label1= new Label(message);
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1,button0, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 200);
        popupwindow.setScene(scene1);
        popupwindow.setTitle("YOU ARE THE CHAMPION");
        popupwindow.getIcons().add(new Image("leo.png"));
        popupwindow.show();
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (started){
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();

//        double totalx = viewModel.getMazeFull().getRows();
//        double totaly = viewModel.getMazeFull().getColumns();
//        double posx = mouseEvent.getSceneX() ;
//        double posy = mouseEvent.getSceneX();
//        double lepposx = mazeDisplayer.getRow_player();
//        double leoposy = mazeDisplayer.getCol_player();
    }

    public void mouseRealesed(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
        double posx = mouseEvent.getSceneX();
        double posy = mouseEvent.getSceneX();
    }

    public void update(Observable o, Object arg) {

        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = viewModel.getMaze();
                this.mazefull = viewModel.getMazeFull();
                drawMaze();
            }
            else {
                int[][] maze = viewModel.getMaze();

                if (maze == this.maze)//Not generateMaze
                {
                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = viewModel.getRowChar();
                    int colFromViewModel = viewModel.getColChar();
                    int rowend = mazefull.getGoalPosition().getRowIndex();
                    int colend = mazefull.getGoalPosition().getColumnIndex();

                    boolean illegal = viewModel.getillegal();

                        mazeDisplayer.setSol(viewModel.getSolution());
                        mazeDisplayer.setPressgen(pressgen);
                        mazeDisplayer.setPresssol(presssol);
                        mazeDisplayer.setStarted(started);
                        mazeDisplayer.draw();

                    if (illegal){
                        viewModel.setillegal();
                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                    }

                    int end = rowend + colend;
                    int cur = rowChar + colChar;
                    if (end == cur - 2 || end == cur + 2)
                    {
                        player1.play();
                        player1 =new MediaPlayer(media1);
                    }

                    if (rowend == rowFromViewModel && colend == colFromViewModel)
                    {

                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);

                        playgoal();
                    }

                    else//Update location
                    {
                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                    }


                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    this.mazefull = viewModel.getMazeFull();
                    drawMaze();
                }
            }
        }
    }

    private void playgoal()
    {
        String video = "resources/gol.mp4";
        Media media = new Media(new File(video).toURI().toString()); //replace /Movies/test.mp3 with your file
        MediaPlayer player = new MediaPlayer(media);
        player.setAutoPlay(true);
        Stage stage = new Stage();
        MediaView mediaView = new MediaView(player);

        Group root = new Group();
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root,1800,1000);
        stage.setScene(scene);
        stage.setTitle("D10S");
        stage.getIcons().add(new Image("leo.png"));
        SetStage1CloseEvent(stage, player);
        stage.show();
    }

    private void SetStage1CloseEvent(Stage primaryStage,MediaPlayer player ) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
               public void handle(WindowEvent windowEvent) {

                    player.stop();
                   String video = "resources/funny.mp4";
                   Media media = new Media(new File(video).toURI().toString()); //replace /Movies/test.mp3 with your file
                   MediaPlayer player = new MediaPlayer(media);
                   player.setAutoPlay(true);
                   Stage stage = new Stage();
                   MediaView mediaView = new MediaView(player);

                   Group root = new Group();
                   root.getChildren().add(mediaView);
                   Scene scene = new Scene(root,550,350);
                   stage.setScene(scene);
                   stage.setTitle("D10S");
                   stage.getIcons().add(new Image("leo.png"));
                   SetStage2CloseEvent(stage, player);
                   stage.show();


               }
           }
        );
    }

    private void SetStage2CloseEvent(Stage primaryStage,MediaPlayer player ) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {

                player.stop();
                showAlert("You win!!! Amazing goal!!!");
       }
   }
        );
    }

    public void drawMaze()
    {
        mazeDisplayer.drawMaze(mazefull);
    }

    public void Help(ActionEvent actionEvent) throws IOException {

        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Help");
        Label label1= new Label("You need to try and get to the goal and score!! \n dont run into Sergio!");
        VBox layout= new VBox(10);
        popupwindow.getIcons().add(new Image("leo.png"));

        layout.getChildren().addAll(label1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 400);
        popupwindow.setScene(scene1);
        popupwindow.show();

    }

    public void About(ActionEvent actionEvent) throws IOException {
        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("About");
        checkproperties();
        popupwindow.getIcons().add(new Image("leo.png"));
        Label label1= new Label("Maze Project created by Ofer and Erez\n" + "The searcing alogrithm used is " + algo +"\nThe maze generator used is " + gen);
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 200);
        popupwindow.setScene(scene1);
        popupwindow.show();

    }


    public void Exit() {
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("do you want to quit? :(");
        Label label1= new Label("Do you really want to exit? :(");
        Button button1= new Button("I quit :((((");
        button1.setOnAction(e -> System.exit(0));
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 400);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

    public void load() throws IOException, ClassNotFoundException {
        Stage stage=new Stage();
        stage.getIcons().add(new Image("leo.png"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            FileInputStream fi = new FileInputStream(new File(selectedFile.getAbsolutePath()));
            ObjectInputStream oi = new ObjectInputStream(fi);

            presssol = false;
            mazefull = (Maze) oi.readObject();
            maze = mazefull.getmap();
            viewModel.setMazefull(mazefull);
            viewModel.setMaze(maze);
            viewModel.solveMaze(this.maze);
            drawMaze();

            oi.close();
            fi.close();
        }
    }

    public void save() throws IOException, ClassNotFoundException {
        if (started) {
            Stage stage = new Stage();
            String mazestr = null;
            stage.getIcons().add(new Image("leo.png"));

            try {
                mazestr = mazefull.getStartPosition() + "" + mazefull.getGoalPosition() + ServerStrategySolveSearchProblem.toHexString(ServerStrategySolveSearchProblem.getSHA(mazefull.toString()));
            } catch (NoSuchAlgorithmException var17) {
                var17.printStackTrace();
            }

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose directory");

            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                File filepath = new File(selectedDirectory.getAbsolutePath(), mazestr + ".txt");

                try (FileOutputStream fos = new FileOutputStream(filepath);
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                    oos.writeObject(mazefull);
                    oos.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public void properties() {
        Group root = new Group();
        double h = 200;
        double w =200;

        Stage popupwindow=new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Properties");
        checkproperties();
        popupwindow.getIcons().add(new Image("leo.png"));
        Label label1= new Label("The searcing alogrithm used is " + algo +"\nThe maze generator used is " + gen +"\nThe number of threads used is " + threads);
        label1.setTextFill(Color.RED);
        VBox layout= new VBox(100);
        layout.setAlignment(Pos.BOTTOM_CENTER);
        layout.getChildren().addAll(label1);
        popupwindow.setResizable(false);
        try {
            Image image1 = new Image(new FileInputStream("resources/1.png"));
            ImageView iv1 = new ImageView();
            iv1.setImage(image1);
            root.getChildren().add(iv1);
            h = image1.getHeight();
            w = image1.getWidth();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        root.getChildren().add(layout);
        layout.setPrefHeight(h/8);
        layout.setPrefWidth(1.6*w);
        Scene scene1= new Scene(root, w, h);
        popupwindow.setScene(scene1);

        popupwindow.show();
    }

    public void checkproperties() {

        try {
            if (Configurations.getgenerator() instanceof EmptyMazeGenerator) {
                gen = "EmptyMazeGenerator";
            } else if (Configurations.getsearching() instanceof SimpleMazeGenerator) {
                gen = "SimpleMazeGenerator";
            } else {
                gen = "MyMazeGenerator";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (Configurations.getsearching() instanceof DepthFirstSearch) {
                algo = "DepthFirstSearch";
            } else if (Configurations.getsearching() instanceof BreadthFirstSearch) {
                algo = "BreadthFirstSearch";
            } else {
                algo = "BestFirstSearch";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threads = Configurations.gethreads();

    }


}
