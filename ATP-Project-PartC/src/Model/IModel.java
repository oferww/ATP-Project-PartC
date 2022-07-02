package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {
    public void generateRandomMaze(int row, int col);

    public int[][] getMaze();

    public void updateCharacterLocation(int direction);

    public int getRowChar();

    public int getColChar();

    public void assignObserver(Observer o);

    public void solveMaze(int[][] maze);

    public Solution getSolution();

    public Maze getMazeFull();

    public void setIllegalfalse();

    public boolean isIllegal();

    public void generatenewRandomMaze(int row, int col);

    public void setMazefull(Maze mazefull) ;

    public void exit();



    public void setMaze(int[][] maze) ;
}