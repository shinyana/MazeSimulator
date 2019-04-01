import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ListIterator;
import javax.swing.JComponent;

/**
 * MazeComponent class
 * <p>
 * A component that displays the maze and path through it if one has been found.
 */
public class MazeComponent extends JComponent {

    private static final int START_X = 10; // top left of corner of maze in frame
    private static final int START_Y = 10;
    private static final int BOX_WIDTH = 20;  // width and height of one maze "location"
    private static final int BOX_HEIGHT = 20;
    private static final int INSET = 2;
    // how much smaller on each side to make entry/exit inner box

    /**
     * <put rep. invar. comment here>
     * 0 <= maze.numRow() < mazeData.length and 0 <= maze.numCol() < mazeData[0].length
     * maze.hasWallAt(new MazeCoord(i,j)) == true or false
     * 0 <= maze.getEntryLoc().getRow() < mazeData.length and 0 <= maze.getEntryLoc().getCol() < mazeData[0].length
     * 0 <= maze.getExitLoc().getRow() < mazeData.length and 0 <= maze.getExitLoc().getCol() < mazeData[0].length
     */

    Maze maze;      //instance variable

    /**
     * Constructs the component.
     *
     * @param maze the maze to display
     */
    public MazeComponent(Maze maze) {

        this.maze = maze;

    }


    /**
     * Draws the current state of maze including the path through it if one has
     * been found.
     *
     * @param g the graphics context
     */
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        //* set inner components of the maze *//
        Rectangle pathAndWall = new Rectangle(START_X, START_Y, BOX_WIDTH, BOX_HEIGHT);

        for (int i = 0; i < this.maze.numRows(); i++) {
            for (int j = 0; j < this.maze.numCols(); j++) {

                if (maze.hasWallAt(new MazeCoord(i, j)) == Maze.WALL) {   //create black box
                    drawRect(g2, pathAndWall, Color.BLACK, true);
                } else {                                                  //create white box
                    drawRect(g2, pathAndWall, Color.WHITE, true);
                }
                pathAndWall.translate(BOX_WIDTH, 0);                   //to avoid creating a lot of rectangle objects
            }
            pathAndWall.translate(-BOX_WIDTH * this.maze.numCols(), BOX_HEIGHT);    //to avoid creating a lot of rectangle objects
        }

        //* set & draw the start location of the maze *//
        Rectangle entryRec = new Rectangle(START_X + this.maze.getEntryLoc().getCol() * BOX_WIDTH + INSET,
                START_Y + this.maze.getEntryLoc().getRow() * BOX_HEIGHT + INSET,
                BOX_WIDTH - INSET - INSET, BOX_HEIGHT - INSET - INSET);

        drawRect(g2, entryRec, Color.YELLOW, true);

        //* set & draw the exit location of the maze *//
        Rectangle exitRec = new Rectangle(START_X + this.maze.getExitLoc().getCol() * BOX_WIDTH + INSET,
                START_Y + this.maze.getExitLoc().getRow() * BOX_HEIGHT + INSET,
                BOX_WIDTH - INSET - INSET, BOX_HEIGHT - INSET - INSET);

        drawRect(g2, exitRec, Color.GREEN, true);

        //* set & draw the outer frame of the maze *//
        Rectangle outerFrame = new Rectangle(START_X, START_Y,
                BOX_WIDTH * this.maze.numCols(), BOX_HEIGHT * this.maze.numRows());

        drawRect(g2, outerFrame, Color.BLACK, false);

        //* set & draw the route from entrance to exit *//
        ListIterator<MazeCoord> iter = maze.getPath().listIterator();

        while (iter.hasNext()) {
            MazeCoord temp1 = iter.next();
            if (iter.hasNext()) {
                MazeCoord temp2 = iter.next();
                Line2D.Double segment = new Line2D.Double(
                        START_X + temp1.getCol() * BOX_WIDTH + BOX_WIDTH / 2,   //"2" means centering the position
                        START_Y + temp1.getRow() * BOX_HEIGHT + BOX_HEIGHT / 2,
                        START_X + temp2.getCol() * BOX_WIDTH + BOX_WIDTH / 2,
                        START_Y + temp2.getRow() * BOX_HEIGHT + BOX_HEIGHT / 2);
                g2.setColor(Color.BLUE);
                g2.draw(segment);
                iter.previous();    //back to connect two points
            }
        }
    }

    /**
     * Draw rectangles in the maze
     *
     * @param g2 the subclass of graphics context
     * @param rect a specification of a rectangular
     * @param color a color to draw a rectangular
     * @param fill whether a rectangular is filled or not
     */

    private void drawRect(Graphics2D g2, Rectangle rect, Color color, boolean fill) {
        g2.setColor(color);
        g2.draw(rect);
        if(fill) {
            g2.fill(rect);
        }
    }
}