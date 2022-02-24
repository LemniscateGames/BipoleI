package lib;

import lib.display.AnimatedValue;
import lib.panels.BattlePanel;

import java.awt.*;

/** A tile that lights up when hovered over. **/
public abstract class ResponsiveTile implements Tile {
    // CONSTANTS
    private static final double BRIGHTNESS_HOVER = 0.25;
    private static final double BRIGHTNESS_MOUSE_HOVER = 0.15;

    // FIELDS
    /** The number that controls the brightness of this tile from -1.0 (black) to 1.0 (white) **/
    private Number brightness;
    /** The number that controls the saturation of this tile from -1.0 (grey) to 1.0 (fully saturated). **/
    private Number saturation;

    /** If this tile is currently hovered over by cursor. **/
    private boolean isHovered;
    /** If this tile is currently hovered over by mouse. **/
    private boolean isMouseHovered;


    public ResponsiveTile() {
        this.brightness = 0.0;
        this.saturation = 0.0;
    }

    public void updateBrightness(){
        final double DESIRED_BRIGHTNESS = (isHovered ? BRIGHTNESS_HOVER : (isMouseHovered ? BRIGHTNESS_MOUSE_HOVER : 0.0));
        if (BattlePanel.EASE_CURSOR) {
            animateToBrightness(BattlePanel.CURSOR_SPEED, DESIRED_BRIGHTNESS);
        } else {
            setBrightness(DESIRED_BRIGHTNESS);
        }
    }

    public void onHover() {
        isHovered = true;
        updateBrightness();
    }

    public void onUnhover() {
        isHovered = false;
        updateBrightness();
    }

    @Override
    public void onMouseHover() {
        isMouseHovered = true;
        updateBrightness();
    }

    @Override
    public void onMouseUnhover() {
        isMouseHovered = false;
        updateBrightness();
    }

    public void animateToBrightness(int speed, double value){
        brightness = new AnimatedValue(speed, brightness.doubleValue(), value);
    }
    public void animateToSaturation(int speed, double value){
        saturation = new AnimatedValue(speed, saturation.doubleValue(), value);
    }

    @Override
    public void drawBelowGrid(Graphics g, double x, double y, double z) {

    }

    public abstract Color getColor();

    public abstract Color getTileColor();

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

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isMouseHovered() {
        return isMouseHovered;
    }
}
