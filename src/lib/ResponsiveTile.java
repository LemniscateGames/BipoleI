package lib;

import lib.display.AnimatedValue;
import lib.panels.BattlePanel;

import java.awt.*;

/** A tile that lights up when hovered over. **/
public abstract class ResponsiveTile implements Tile {
    // CONSTANTS
    private static final double BRIGHTNESS_HOVER = 0.25;

    // FIELDS
    /** The number that controls the brightness of this tile from -1.0 (black) to 1.0 (white) **/
    private Number brightness;
    /** The number that controls the saturation of this tile from -1.0 (grey) to 1.0 (fully saturated). **/
    private Number saturation;

    public ResponsiveTile() {
        this.brightness = 0.0;
        this.saturation = 0.0;
    }

    public void onHover() {
        if (BattlePanel.EASE_CURSOR) {
            animateToBrightness(BattlePanel.CURSOR_SPEED, BRIGHTNESS_HOVER);
        } else {
            setBrightness(1.0);
        }
    }

    public void onUnhover() {
        if (BattlePanel.EASE_CURSOR) {
            animateToBrightness(BattlePanel.CURSOR_SPEED, 0.0);
        } else {
            setBrightness(0.0);
        }
    }

    public void animateToBrightness(int speed, double value){
        brightness = new AnimatedValue(speed, brightness.doubleValue(), value);
    }
    public void animateToSaturation(int speed, double value){
        saturation = new AnimatedValue(speed, saturation.doubleValue(), value);
    }

    public abstract Color getColor();

    public abstract Color getUnitColor();

    public Number getBrightness() {
        return brightness;
    }
    public void setBrightness(Number brightness) {
        this.brightness = brightness;
    }
    public Number getSaturation() {
        return saturation;
    }
    public void setSaturation(Number saturation) {
        this.saturation = saturation;
    }
}
