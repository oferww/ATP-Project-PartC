package View;

import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;

public class MyViewController implements Initializable {
    public MazeGenerator generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;

    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();


    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) {
        if(generator == null)
            generator = new MazeGenerator();

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        Maze maze = generator.generateRandomMaze(rows, cols);

        mazeDisplayer.drawMaze(maze);
        setPlayerPosition(0, 0);
    }

    public void solveMaze(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        int row = mazeDisplayer.getRow_player();
        int col = mazeDisplayer.getCol_player();

        switch (keyEvent.getCode()) {
            case UP:
                 row -= 1;
                 break;
            case DOWN:
                 row += 1;
                 break;
            case RIGHT:
                 col += 1;
                 break;
            case LEFT:
                 col -= 1;
                 break;
        }
        setPlayerPosition(row, col);

        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col){
        mazeDisplayer.set_player_position(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
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
}
