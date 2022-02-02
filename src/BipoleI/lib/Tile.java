package BipoleI.lib;

import java.awt.*;

public interface Tile {
    /** Draw the grid square for this tile only.
     * Drawn after the white grid is drawn but before cursor and units. **/
    void drawGridTile(Graphics g, int x, int y, int z, boolean brighter);

    /** Draw this tile's unit. Drawn after the cursor is drawn. **/
    void draw(Graphics g, int x, int y, int z, boolean brighter);
}
