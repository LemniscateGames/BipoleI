package lib;

import java.awt.*;

public interface Tile {
    /** Draw the base of this tile. Drawn below the cursor. **/
    void drawTileBase(Graphics g, double x, double y, double z);

    /** Draw this tile's features. Drawn above the cursor. **/
    void draw(Graphics g, double x, double y, double z);

    /** Run when the cursor is hovered over tile. **/
    void onHover();

    /** Run when the cursor is moved off of hovering over this tile. **/
    void onUnhover();
}
