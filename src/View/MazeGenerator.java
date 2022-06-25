package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.util.Arrays;

public class MazeGenerator {
    public static void main(String[] args) {
        MazeGenerator generator = new MazeGenerator();
        Maze maze = generator.generateRandomMaze(5, 5);
        System.out.println(maze);
    }

    public Maze generateRandomMaze(int rows, int cols){
        Maze maze = new MyMazeGenerator().generate(rows,cols);
        return maze;
    }
}
