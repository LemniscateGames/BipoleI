package lib;

import lib.display.AnimatedValue;
import lib.panels.BattlePanel;

import java.awt.*;

/** A tile that lights up when hovered over. **/
public abstract class ResponsiveTile implements Tile {
    /** The number that controls the brightness of this tile when hovered over. Set to an AnimatedValue when onHover() is called. **/
    private Number hoverBrightness;

    public ResponsiveTile() {
        this.hoverBrightness = 0.0;
    }

    public void onHover() {
        if (BattlePanel.EASE_CURSOR) {
            animateToBrightness(BattlePanel.CURSOR_SPEED, 1.0);
        } else {
            setHoverBrightness(1.0);
        }
    }

    public void onUnhover() {
        if (BattlePanel.EASE_CURSOR) {
            animateToBrightness(BattlePanel.CURSOR_SPEED, 0.0);
        } else {
            setHoverBrightness(0.0);
        }
    }

    public void animateToBrightness(int speed, double value){
        hoverBrightness = new AnimatedValue(speed, hoverBrightness.doubleValue(), value);
    }

    public abstract Color getColor();

    public abstract Color getUnitColor();

    public Number getHoverBrightness() {
        return hoverBrightness;
    }

    public void setHoverBrightness(Number hoverBrightness) {
        this.hoverBrightness = hoverBrightness;
    }
}
