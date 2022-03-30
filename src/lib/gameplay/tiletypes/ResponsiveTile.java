package lib.gameplay.tiletypes;

import lib.Tile;
import lib.display.AnimatedValue;
import lib.display.TimingFunction;
import lib.panels.BattlePanel;
import lib.ui.ElementBox;
import lib.ui.TileInfoElementBox;

import java.awt.*;

/** A tile that lights up when hovered over. **/
public abstract class ResponsiveTile implements Tile {
    // CONSTANTS
    public static final int MOUSE_HOVER_SPEED = 75;
    public static final double CURSOR_HOVER_GRID_BRIGHTNESS = 0.25;
    public static final double CURSOR_HOVER_GRID_SATURATION = 0.125;
    public static final double MOUSE_HOVER_GRID_BRIGHTNESS = 0.15;
    public static final double MOUSE_HOVER_GRID_SATURATION = 0.075;
    private static final double BRIGHTNESS_HOVER = 0.25;
    private static final double BRIGHTNESS_MOUSE_HOVER = 0.15;
    public static final Color DIM_COLOR = new Color(32, 32,32, 192);
    public static final Color LIGHT_COLOR = new Color(250, 250,220, 192);

    // FIELDS
    /** The number that controls the brightness of this tile from -1.0 (black) to 1.0 (white) **/
    private Number brightness;
    /** The number that controls the saturation of this tile from -1.0 (grey) to 1.0 (fully saturated). **/
    private Number saturation;
    /** Number that dims this tile when other tiles are focused on. 0.0 (not dim) to 1.0 (fully dimmed). **/
    private Number dimness;
    /** Whether or not to draw this tile's base after other tiles. **/
    private boolean baseDrawPriority;

    /** If this tile is currently hovered over by cursor. **/
    private boolean isHovered;
    /** If this tile is currently hovered over by mouse. **/
    private boolean isMouseHovered;

    /** The brightness of this tile's grid square, animated on hover and unhover. **/
    private Number gridBrightness = 0.0;
    /** The brightness of this tile's grid square, animated on hover and unhover. **/
    private Number gridSaturation = 0.0;

    public ResponsiveTile() {
        this.brightness = 0.0;
        this.saturation = 0.0;
        this.dimness = 0.0;
    }

    public void updateBrightness(){
        final double DESIRED_BRIGHTNESS = (isHovered ? BRIGHTNESS_HOVER : (isMouseHovered ? BRIGHTNESS_MOUSE_HOVER : 0.0));
        if (BattlePanel.EASE_CURSOR) {
            animateToBrightness(BattlePanel.CURSOR_SPEED, DESIRED_BRIGHTNESS);
        } else {
            setBrightness(DESIRED_BRIGHTNESS);
        }
    }

    public void updateGridColors(){
        final double DESIRED_BRIGHTNESS = isHovered() ? CURSOR_HOVER_GRID_BRIGHTNESS : (isMouseHovered() ? MOUSE_HOVER_GRID_BRIGHTNESS : 0.0);
        final double DESIRED_SATURATION = isHovered() ? CURSOR_HOVER_GRID_SATURATION : (isMouseHovered() ? MOUSE_HOVER_GRID_SATURATION : 0.0);
        gridBrightness = new AnimatedValue(TimingFunction.LINEAR, MOUSE_HOVER_SPEED, gridBrightness.doubleValue(), DESIRED_BRIGHTNESS);
        gridSaturation = new AnimatedValue(TimingFunction.LINEAR, MOUSE_HOVER_SPEED, gridSaturation.doubleValue(), DESIRED_SATURATION);
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
    public Number getDimness() {
        return dimness;
    }
    public void setDimness(Number dimness) {
        this.dimness = dimness;
    }

    public boolean isBaseDrawPriority() {
        return baseDrawPriority;
    }

    public void setBaseDrawPriority(boolean baseDrawPriority) {
        this.baseDrawPriority = baseDrawPriority;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isMouseHovered() {
        return isMouseHovered;
    }

    public Number getGridBrightness() {
        return gridBrightness;
    }

    public Number getGridSaturation() {
        return gridSaturation;
    }
}
