import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;


/**
 * MazeViewer class
 * <p>
 * Program to read in and display a maze and a path through the maze. At user
 * command displays a path through the maze if there is one.
 * <p>
 * How to call it from the command line:
 * <p>
 * java MazeViewer mazeFile
 * <p>
 * where mazeFile is a text file of the maze. The format is the number of rows
 * and number of columns, followed by one line per row, followed by the start location,
 * and ending with the exit location. Each maze location is
 * either a wall (1) or free (0). Here is an example of contents of a file for
 * a 3x4 maze, with start location as the top left, and exit location as the bottom right
 * (we count locations from 0, similar to Java arrays):
 * <p>
 * 3 4
 * 0111
 * 0000
 * 1110
 * 0 0
 * 2 3
 */

public class MazeViewer {

    private static final char WALL_CHAR = '1';

    public static void main(String[] args) {

        String fileName = "";

        try {

            if (args.length < 1) {
                System.out.println("ERROR: missing file name command line argument");
            } else {
                fileName = args[0];

                JFrame frame = readMazeFile(fileName);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                frame.setVisible(true);
            }

        } catch (FileNotFoundException exc) {
            System.out.println("ERROR: File not found: " + fileName);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * readMazeFile reads in maze from the file whose name is given and
     * returns a MazeFrame created from it.
     *
     * @param fileName the name of a file to read from (file format shown in class comments, above)
     * @throws FileNotFoundException if there's no such file (subclass of IOException)
     * @throws IOException           (hook given in case you want to do more error-checking --
     *                               that would also involve changing main to catch other exceptions)
     * @returns a MazeFrame containing the data from the file.
     */
    private static MazeFrame readMazeFile(String fileName) throws IOException {

        String pathFileName = "testfiles\\" + fileName;     //assign relative path for IntelliJ
//        String pathFileName = "testfiles/" + fileName;    //assign relative path for linux env
        Scanner in = new Scanner(new File(pathFileName));   //open a file

        //* Get size of maze in the text file *//
        int numRow = in.nextInt();
        int numCol = in.nextInt();
        in.nextLine();            //consume newline

        //* read maze data in the text file *//
        boolean[][] mapOfMaze = new boolean[numRow][numCol];    //initial condition: all elements are false

        for (int i = 0; i < numRow; i++) {
            String rowLine = in.nextLine();            //read one row
            for (int j = 0; j < numCol; j++) {
                if (rowLine.charAt(j) == WALL_CHAR) {  //data is "1" -> WALL(true)
                    mapOfMaze[i][j] = Maze.WALL;
                }
            }
        }
        //* read entry coordinates and exit coordinates in the text file *//

        return new MazeFrame(mapOfMaze,
                new MazeCoord(in.nextInt(), in.nextInt()), new MazeCoord(in.nextInt(), in.nextInt()));

    }
}