import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

/**
 * A Canvas use to draw the room map
 *
 * @author XIAOWEI ZHANG
 */
public class Cartographer extends javafx.scene.canvas.Canvas {
    private GraphicsContext gc;

    /**
     * @param width  The width of Canvas
     * @param height The height of Canvas
     */
    public Cartographer(double width, double height) {
        super(width, height);
        gc = getGraphicsContext2D();

    }

    /**
     * Draw each room by 40x40 rectangular in canvas
     *
     * @param x The x coordinate of top left corner of the rectangular in canvas
     * @param y The y coordinate of top left corner of the rectangular in canvas
     */
    public void drawRoom(int x, int y) {
        gc.strokeRect(x, y, 40, 40);
    }

    /**
     * Draw the player by @
     *
     * @param x The x coordinate of the player in canvas
     * @param y The y coordinate of the player in canvas
     */
    public void drawPlayer(int x, int y) {

        gc.fillText("@", x, y);

    }

    /**
     * Clear the canvas
     *
     * @param x The x coordinate of top left corner of the canvas
     * @param y The y coordinate of top left corner of the  canvas
     */
    public void clear(int x, int y) {
        gc.clearRect(0, 0, x, y);
    }

    /**
     * Draw alive critter by M
     *
     * @param x The x coordinate of the alive critter in canvas
     * @param y The y coordinate of the alive critter in canvas
     */
    public void drawCritter(int x, int y) {

        gc.fillText("M", x, y);

    }

    /**
     * Draw treasure by $
     *
     * @param x The x coordinate of the treasure in canvas
     * @param y The y coordinate of the treasure in canvas
     */
    public void drawTreasure(int x, int y) {
        gc.fillText("$", x, y);

    }

    /**
     * Draw dead critter by m
     *
     * @param x The x coordinate of the dead critter in canvas
     * @param y The y coordinate of the dead critter in canvas
     */
    public void drawDeadCritter(int x, int y) {

        gc.fillText("m", x, y);

    }

    /**
     * Draw exit by a short line
     *
     * @param x1 The x coordinate of the line start point in canvas
     * @param y1 The y coordinate of the line start point in canvas
     * @param x2 The x coordinate of the line end point in canvas
     * @param y2 The x coordinate of the line end point in canvas
     */
    public void drawExit(int x1, int y1, int x2, int y2) {
        gc.strokeLine(x1, y1, x2, y2);
    }
}
