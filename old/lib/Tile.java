package old.BipoleI.lib;

import java.awt.*;

public abstract class Tile {
    private Number brightness = 0.0;
    private Number grayness = 0.6;

    /** Draw the grid square for this tile only.
     * Drawn after the white grid is drawn but before cursor and units. **/
    public abstract void drawGridTile(Graphics g, double x, double y, double z);

    /** Draw this tile's unit. Drawn after the cursor is drawn. **/
    public abstract void draw(Graphics g, double x, double y, double z);

    /** Draw the UI for this tile (HP, ATK, Readiness, etc...) **/
    public abstract void drawUI(Graphics g, double x, double y, double z);

    public abstract boolean canAct();

    public Number getBrightness() {
        return brightness;
    }

    public void setBrightness(Number brightness) {
        this.brightness = brightness;
    }

    public Number getGrayness() {
        return grayness;
    }

    public void setGrayness(Number grayness) {
        this.grayness = grayness;
    }
}
