package lib;

import lib.display.AnimatedValue;
import lib.display.TimingFunction;
import lib.display.shaperendering.ShapeOrtho3D;
import lib.panels.BattlePanel;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;

public abstract class ClaimedTile extends GeometryTile {
    public static final int MOUSE_HOVER_SPEED = 100;
    public static final double CURSOR_HOVER_GRID_BRIGHTNESS = 0.25;
    public static final double CURSOR_HOVER_GRID_SATURATION = 0.125;
    public static final double MOUSE_HOVER_GRID_BRIGHTNESS = 0.15;
    public static final double MOUSE_HOVER_GRID_SATURATION = 0.075;

    /** The team that claimed this tile. **/
    private final Team team;
    /** The map this unit is placed on. Set when onPlace() is called. **/
    private Map map;
    /** This tile's row on the map it is placed on. **/
    private int row;
    /** This tile's column on the map it is placed on. **/
    private int column;

    /** The brightness of this tile's grid square, animated on hover and unhover. **/
    private Number gridBrightness = 0.0;
    /** The brightness of this tile's grid square, animated on hover and unhover. **/
    private Number gridSaturation = 0.0;

    public ClaimedTile(Team team, ShapeOrtho3D... shapes){
        super(shapes);
        this.team = team;
    }

    @Override
    public void onPlace(Map map, int r, int c) {
        this.map = map;
        row = r;
        column = c;
    }

    @Override
    public void drawTileBase(Graphics g, double x, double y, double z) {
        BattlePanel.drawTile(g, x, y, z, getTileColor(), getTileFillColor());
    }

    @Override
    public boolean hasBorder() {
        return true;
    }

    @Override
    public Color getColor() {
        return team.getColor(getBrightness().doubleValue(), getSaturation().doubleValue());
    }

    @Override
    public Color getTileColor(){
        return team.getColor(gridBrightness.doubleValue(), gridSaturation.doubleValue());
    }

    public Color getTileFillColor(){
        return team.getTileFillColor(gridBrightness.doubleValue()*.3, gridSaturation.doubleValue()*.3);
    }

    @Override
    public Color getUnitColor() {
        return team.getUnitColor();
    }

    @Override
    public void onHover() {
        super.onHover();
        updateGridColors();
    }

    @Override
    public void onUnhover() {
        super.onUnhover();
        updateGridColors();
    }

    @Override
    public void onMouseHover() {
        super.onMouseHover();
        updateGridColors();

    }

    @Override
    public void onMouseUnhover() {
        super.onMouseUnhover();
        updateGridColors();
    }

    public void updateGridColors(){
        final double DESIRED_BRIGHTNESS = isHovered() ? CURSOR_HOVER_GRID_BRIGHTNESS : (isMouseHovered() ? MOUSE_HOVER_GRID_BRIGHTNESS : 0.0);
        final double DESIRED_SATURATION = isHovered() ? CURSOR_HOVER_GRID_SATURATION : (isMouseHovered() ? MOUSE_HOVER_GRID_SATURATION : 0.0);
        gridBrightness = new AnimatedValue(TimingFunction.LINEAR, MOUSE_HOVER_SPEED, gridBrightness.doubleValue(), DESIRED_BRIGHTNESS);
        gridSaturation = new AnimatedValue(TimingFunction.LINEAR, MOUSE_HOVER_SPEED, gridSaturation.doubleValue(), DESIRED_SATURATION);
    }

    // accessors
    public Team getTeam() {
        return team;
    }
    public Map getMap() {
        return map;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
}
