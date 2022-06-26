package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements Observer, IView {

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

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        lbl_player_row.textProperty().bind(update_player_position_row);
        lbl_player_column.textProperty().bind(update_player_position_col);
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
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows,cols);


    }

    public void solveMaze()
    {
        viewModel.solveMaze(this.maze);

    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);;
        alert.show();
    }

    public void keyPressed(KeyEvent keyEvent) {

        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = viewModel.getMaze();
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

                    if(rowFromViewModel == rowChar && colFromViewModel == colChar)//Solve Maze
                    {
                        viewModel.getSolution();
                        showAlert("Solving Maze ... ");
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
                    drawMaze();
                }
            }
        }
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
        Label label1= new Label("Maze Project created by Ofer and Erez");
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 400);
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
}
