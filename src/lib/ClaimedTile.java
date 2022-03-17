package lib;

import lib.display.shaperendering.ShapeOrtho3D;
import lib.panels.BattlePanel;
import lib.ui.TileInfoElementBox;

import java.awt.*;

public abstract class ClaimedTile extends GeometryTile {
    /** The team that claimed this tile. **/
    private final Team team;
    /** The map this unit is placed on. Set when onPlace() is called. **/
    private Map map;
    /** This tile's row on the map it is placed on. **/
    private int row;
    /** This tile's column on the map it is placed on. **/
    private int column;

    public ClaimedTile(){
        this.team = null;
    }
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
    public void displayInfo(TileInfoElementBox element) {
        super.displayInfo(element);
        element.getTitle().setFg(getTeam().getColor(0.25, 0));
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
        return team.getColor(getGridBrightness().doubleValue(), getGridBrightness().doubleValue());
    }

    public Color getTileFillColor(){
        return team.getTileFillColor(getGridBrightness().doubleValue()*.3, getGridBrightness().doubleValue()*.3);
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
