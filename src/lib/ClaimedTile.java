package lib;

import lib.display.shaperendering.ShapeOrtho3D;
import lib.panels.BattlePanel;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;

public abstract class ClaimedTile extends GeometryTile {
    /** The team that claimed this tile. **/
    private final Team team;
    /** The map this unit is placed on. Set when onPlace() is called. **/
    private Map map;
    /** This tile's row on the map it is placed on. **/
    private int row;
    /** This tile's column on the map it is placed on. **/
    private int column;

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
        BattlePanel.drawTile(g, x, y, z, getColor(), team.getTileFillColor());
    }

    @Override
    public boolean hasBorder() {
        return true;
    }

    @Override
    public Color getColor() {
        Color color = team.getColor();

        if (getBrightness().doubleValue() > 0){
            color = blendColors(color, Color.WHITE, getBrightness().doubleValue()*1);
        } else if (getBrightness().doubleValue() < 0){
            color = blendColors(color, Color.BLACK, getBrightness().doubleValue()*-1);
        }

        if (getSaturation().doubleValue() > 0){
            color = blendColors(color, Color.YELLOW, getSaturation().doubleValue()*1);
        } else if (getSaturation().doubleValue() < 0){
            color = blendColors(color, Color.DARK_GRAY, getSaturation().doubleValue()*-1);
        }

        return color;
    }

    @Override
    public Color getUnitColor() {
        return team.getUnitColor();
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
