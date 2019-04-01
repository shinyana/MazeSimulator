import java.util.LinkedList;


/**
 * Maze class
 * <p>
 * Stores information about a maze and can find a path through the maze
 * (if there is one).
 * <p>
 * Assumptions about structure of the maze, as given in mazeData, startLoc, and endLoc
 * (parameters to constructor), and the path:
 * -- no outer walls given in mazeData -- search assumes there is a virtual
 * border around the maze (i.e., the maze path can't go outside of the maze
 * boundaries)
 * -- start location for a path is maze coordinate startLoc
 * -- exit location is maze coordinate exitLoc
 * -- mazeData input is a 2D array of booleans, where true means there is a wall
 * at that location, and false means there isn't (see public FREE / WALL
 * constants below)
 * -- in mazeData the first index indicates the row. e.g., mazeData[row][col]
 * -- only travel in 4 compass directions (no diagonal paths)
 * -- can't travel through walls
 */


public class Maze {

    public static final boolean FREE = false;
    public static final boolean WALL = true;

    /**
     * <put rep. invar. comment here>
     * 0 <= startLocMaze.getRow() < mazeData.length and 0 <= startLocMaze.getCol() < mazeData[0].length
     * 0 <= exitLocMaze.getRow() < mazeData.length and 0 <= exitLocMaze.getCol() < mazeData[0].length
     * 0 < mazeData.length
     *
     */

    //* instance variables *//
    boolean[][] mazeData;
    MazeCoord startLocMaze;
    MazeCoord exitLocMaze;
    LinkedList<MazeCoord> path;

    /**
     * Constructs a maze.
     *
     * @param mazeData the maze to search.  See general Maze comments above for what
     *                 goes in this array.
     * @param startLoc the location in maze to start the search (not necessarily on an edge)
     * @param exitLoc  the "exit" location of the maze (not necessarily on an edge)
     *                 PRE: 0 <= startLoc.getRow() < mazeData.length and 0 <= startLoc.getCol() < mazeData[0].length
     *                 and 0 <= endLoc.getRow() < mazeData.length and 0 <= endLoc.getCol() < mazeData[0].length
     */
    public Maze(boolean[][] mazeData, MazeCoord startLoc, MazeCoord exitLoc) {

        this.mazeData = mazeData;
        startLocMaze = startLoc;
        exitLocMaze = exitLoc;
        path = new LinkedList<>();

    }


    /**
     * Returns the number of rows in the maze
     *
     * @return number of rows
     */
    public int numRows() {

//        return mazeData.length;
        return mazeData.length;

    }


    /**
     * Returns the number of columns in the maze
     *
     * @return number of columns
     */
    public int numCols() {

        return mazeData[0].length;

    }


    /**
     * Returns true iff there is a wall at this location
     *
     * @param loc the location in maze coordinates
     * @return whether there is a wall here
     * PRE: 0 <= loc.getRow() < numRows() and 0 <= loc.getCol() < numCols()
     */
    public boolean hasWallAt(MazeCoord loc) {

        return mazeData[loc.getRow()][loc.getCol()];

    }


    /**
     * Returns the entry location of this maze.
     */
    public MazeCoord getEntryLoc() {

        return startLocMaze;

    }


    /**
     * Returns the exit location of this maze.
     */
    public MazeCoord getExitLoc() {

        return exitLocMaze;

    }


    /**
     * Returns the path through the maze. First element is start location, and
     * last element is exit location.  If there was not path, or if this is called
     * before a call to search, returns empty list.
     *
     * @return the maze path
     */
    public LinkedList<MazeCoord> getPath() {

        return (LinkedList<MazeCoord>) path.clone();  //copy LL to avoid side effect because client can access it
        //return path;
    }


    /**
     * Find a path from start location to the exit location (see Maze
     * constructor parameters, startLoc and exitLoc) if there is one.
     * Client can access the path found via getPath method.
     *
     * @return whether a path was found.
     */
    public boolean search() {
        path.clear();   //clear the LinkedList for searching more than twice

        //* initialize the current location*//
        int currentRow = startLocMaze.getRow();
        int currentCol = startLocMaze.getCol();

        //* 2D array for marking where it has already been visited : visited -> WALL(true), not visited -> FREE(false) *//
        boolean[][] markLoc = new boolean[mazeData.length][mazeData[0].length]; //initial condition: all elements are false

        return findPath(new MazeCoord(currentRow, currentCol), markLoc);
    }

    /**
     * find a path recursively
     *
     * @param loc     the current location in the maze
     * @param markLoc locations where we have already visited
     * @return boolean find a path of not
     */

    private boolean findPath(MazeCoord loc, boolean markLoc[][]) {

        //* base cases *//
        if (0 > loc.getRow() || loc.getRow() >= mazeData.length ||
                0 > loc.getCol() || loc.getCol() >= mazeData[0].length ||
                mazeData[loc.getRow()][loc.getCol()] == WALL) {     //check current location is a wall or not, or outside of the maze
            return false;
        } else if (markLoc[loc.getRow()][loc.getCol()] == WALL) {   //check whether current location has already been accessed or not
            return false;
        } else if (loc.getRow() == exitLocMaze.getRow() && loc.getCol() == exitLocMaze.getCol()) {  //check whether current location is exit or not
            path.add(loc);
            return true;
        }

        //* set the mark as WALL when the location has already been visited : visited -> WALL(true), not visited -> FREE(false)*//
        markLoc[loc.getRow()][loc.getCol()] = WALL;

        //// search order ////////////////////////////////////////////////
        //                  |           1. (r - 1, c)  |                //
        //------------------|--------------------------|----------------//
        //   2. (r, c - 1)  |   current loc (r    , c) | 4. (r, c + 1)  //
        //------------------|--------------------------|----------------//
        //                  |           3. (r + 1, c)  |                //
        //////////////////////////////////////////////////////////////////

        for (int i = -1; i <= 1; i = i + 2) {
            if ((loc.getRow() + i < mazeData.length && loc.getRow() + i >= 0) &&        //to avoid referring out of the bound of the array
                    mazeData[loc.getRow() + i][loc.getCol()] == FREE &&                 //check whether next position is WALL or not
                    markLoc[loc.getRow() + i][loc.getCol()] == FREE                     //check whether next position has been already visited or not
                    ) {
                if (findPath(new MazeCoord(loc.getRow() + i, loc.getCol()), markLoc) == true) {
                    path.add(0,loc);    //add coordinates of the path at the beginning of the list
                    return true;
                }
            }
            if ((loc.getCol() + i >= 0 && loc.getCol() + i < mazeData[0].length) &&     //to avoid referring out of the bound of the array
                    mazeData[loc.getRow()][loc.getCol() + i] == FREE &&                 //check whether next position is WALL or not
                    markLoc[loc.getRow()][loc.getCol() + i] == FREE                     //check whether next position has been already visited or not
                    ) {
                if (findPath(new MazeCoord(loc.getRow(), loc.getCol() + i), markLoc) == true) {
                    path.add(0,loc);    //add coordinates of the path at the beginning of the list
                    return true;
                }
            }
        }
        return false;
    }
}