package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private Maze mazefull;
    private int[][] maze;
    private int row_player =0;
    private int col_player =0;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNamegoal = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamegoal() {
        return imageFileNamegoal.get();
    }

    public void setImageFileNamegoal(String imageFileNamegoal) {
        this.imageFileNamegoal.set(imageFileNamegoal);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }




    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;

        draw();

    }




    public void drawMaze(Maze maze)
    {
        mazefull = maze;
        this.maze = maze.getmap();
        draw();
    }

    public void draw()
    {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = mazefull.getRows();
            int col = mazefull.getColumns();
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze
            Image wallImage = null;
            Image goalImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
                goalImage = new Image(new FileInputStream(getImageFileNamegoal()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no wall or goal file....");
            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    h = i * cellHeight;
                    w = j * cellWidth;

                    if (mazefull.getGoalPosition().getRowIndex() == i && mazefull.getGoalPosition().getColumnIndex() == j)
                    {
                        if ( goalImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }
                        else {
                            graphicsContext.drawImage(goalImage, w, h, cellWidth, cellHeight);
                        }
                    }
                    if(mazefull.getval(i,j) == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){

                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }

                }
            }

            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

        }
    }
}
