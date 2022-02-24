package lib;

import java.awt.*;

/** Any tile that can be placed on a map. **/
public interface Tile {
    /** Anything to draw below the grid (such as contested tile diagonal lines). **/
    void drawBelowGrid(Graphics g, double x, double y, double z);

    /** Draw the base of this tile. Drawn below the cursor. **/
    void drawTileBase(Graphics g, double x, double y, double z);

    /** Draw this tile's features. Drawn above the cursor. **/
    void draw(Graphics g, double x, double y, double z);

    /** Draw the UI for this tile (readiness, hp, atk, etc). **/
    void drawUI(Graphics g, double x, double y, double z);

    /** Run when the cursor is hovered over tile. **/
    void onHover();

    /** Run when the cursor is moved off of hovering over this tile. **/
    void onUnhover();

    /** Run when the mouse is hovered over tile. **/
    void onMouseHover();

    /** Run when the mouse is moved off of hovering over this tile. **/
    void onMouseUnhover();

    /** Whether or not this tile draws a tile base (colored border). **/
    boolean hasBorder();

    /** Ran when this tile is placed on a map. **/
    void onPlace(Map map, int r, int c);
}
