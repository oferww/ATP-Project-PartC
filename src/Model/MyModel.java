package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Client.IClientStrategy;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MyModel extends Observable implements IModel{

    private int[][] maze;
    private Maze mazefull;
    private int rowChar;
    private int colChar;
    Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
    Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());

    public MyModel() {
        maze = null;
        rowChar =0;
        colChar =0;
        mazeGeneratingServer.start();
    }

    public void updateCharacterLocation(int direction)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
         */

        switch(direction)
        {
            case 1:
            case 18://Up
                //if(rowChar!=0)
                rowChar--;
                break;

            case 2:
            case 12://Down
                //  if(rowChar!=maze.length-1)
                rowChar++;
                break;
            case 3://Left
            case 14:    //  if(colChar!=0)
                colChar--;
                break;
            case 4:
            case 16://Right
                //  if(colChar!=maze[0].length-1)
                colChar++;
                break;
            case 11:
                colChar--;
                rowChar--;
                break;

        }

        setChanged();
        notifyObservers();
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze(int[][] maze) {
        //Solving maze
        setChanged();
        notifyObservers();
    }

    @Override
    public void getSolution() {
        //return this.solution;
    }


    public void generateRandomMaze(int row, int col)
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(row * col ) + 25];
                        is.read(decompressedMaze);
                        mazefull = new Maze(decompressedMaze);
                        maze = mazefull.getmap();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

        setChanged();
        notifyObservers();
    }

    public int[][] getMaze() {
        return maze;
    }
    public Maze getMazeFull() {
        return mazefull;
    }
}
