package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int [][] maze;
    private int rowChar;
    private int colChar;
    private Maze mazefull;


    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
        this.mazefull = null;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
        model.setMaze(maze);
    }

    public void setMazefull(Maze mazefull) {
        this.mazefull = mazefull;
        model.setMazefull(mazefull);
    }

    public int[][] getMaze() {
        return maze;
    }

    public Maze getMazeFull() {
        return mazefull;
    }


    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public boolean getillegal()
    {
        return model.isIllegal();
    }

    public void setillegal()
    {
        model.setIllegalfalse();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = model.getMaze();
                this.mazefull = model.getMazeFull();
            }
            else {
                int[][] maze = model.getMaze();
                Maze mazefull = model.getMazeFull();

                if (maze == this.maze)//Not generateMaze
                {
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    if(this.colChar == colChar && this.rowChar == rowChar)//Solve Maze
                    {
                        model.getSolution();
                    }
                    else//Update location
                    {
                        this.rowChar = rowChar;
                        this.colChar = colChar;
                    }


                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    this.mazefull = mazefull;

                }
            }

            setChanged();
            notifyObservers();
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.generateRandomMaze(row,col);
    }

    public void generatenewMaze(int row,int col)
    {
        this.model.generatenewRandomMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {

        int direction = -1;

        switch (keyEvent.getCode()){
            case UP:
                direction = 1;
                break;
            case DOWN:
                direction = 2;
                break;
            case LEFT:
                direction = 3;
                break;
            case RIGHT:
                direction = 4;
                break;
            case NUMPAD1:
                direction = 11;
                break;
            case NUMPAD2:
                direction = 12;
                break;
            case NUMPAD3:
                direction = 13;
                break;
            case NUMPAD4:
                direction = 14;
                break;
            case NUMPAD6:
                direction = 16;
                break;
            case NUMPAD7:
                direction = 17;
                break;
            case NUMPAD8:
                direction = 18;
                break;
            case NUMPAD9:
                direction = 19;
                break;

        }

        model.updateCharacterLocation(direction);
    }

    public void solveMaze(int [][] maze)
    {
        model.solveMaze(maze);
    }

    public Solution getSolution()
    {
        return model.getSolution();
    }
}
