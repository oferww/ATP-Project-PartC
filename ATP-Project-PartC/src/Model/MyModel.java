package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Client.IClientStrategy;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MyModel extends Observable implements IModel{

    private int[][] maze;
    private Maze mazefull;
    private int rowChar;
    private int colChar;



    boolean illegal = false;
    Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
    Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    Solution sol;



    public MyModel() {
        maze = null;
        rowChar =0;
        colChar =0;
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();
    }


    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public void setMazefull(Maze mazefull) {
        this.mazefull = mazefull;
    }
    public boolean isIllegal() {
        return illegal;
    }

    public void setIllegalfalse() {
        this.illegal = false;
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
                if(rowChar!=0 && maze[rowChar-1][colChar] != 1) {
                    rowChar--;
                }
                break;

            case 2:
            case 12://Down
                if(rowChar!=maze.length-1 && maze[rowChar+1][colChar] != 1) {
                    rowChar++;
                }
                break;
            case 3://Left
            case 14:    //
                 if(colChar!=0 && maze[rowChar][colChar-1] != 1) {
                     colChar--;
                 }
                 break;
            case 4:
            case 16://Right
                if(colChar!=maze[0].length-1 && maze[rowChar][colChar+1] != 1) {
                    colChar++;
                }
                break;
            case 11:
                if (colChar!=0 && rowChar!=maze.length-1 && maze[rowChar+1][colChar-1] != 1) {
                    colChar--;
                    rowChar++;
                }
                break;
            case 13:
                if (colChar!=maze[0].length-1 && rowChar!=maze.length-1 && maze[rowChar+1][colChar+1] != 1) {
                    colChar++;
                    rowChar++;
                }
                break;
            case 17:
                if (colChar!=0 && rowChar!=0 && maze[rowChar-1][colChar-1] != 1) {
                    colChar--;
                    rowChar--;
                }
                break;
            case 19:
                if (colChar!=maze[0].length-1 && rowChar!=0 && maze[rowChar-1][colChar+1] != 1) {
                    colChar++;
                    rowChar--;
                }
                break;
            default:
                illegal = true;
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

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(mazefull);
                        toServer.flush();
                        sol = (Solution)fromServer.readObject();
                        System.out.println(String.format("Solution steps: %s", sol));

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

    @Override
    public Solution getSolution() {
        return this.sol;
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

    public void generatenewRandomMaze(int row, int col)
    {
        rowChar=0;
        colChar=0;
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

    public void exit()
    {
        solveSearchProblemServer.stop();
        mazeGeneratingServer.stop();
    }
}
