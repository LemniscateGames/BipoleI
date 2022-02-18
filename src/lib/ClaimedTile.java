package lib;

import lib.display.shaperendering.ShapeOrtho3D;
import lib.panels.BattlePanel;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;

public class ClaimedTile extends GeometryTile {
    /** The team that claimed this tile. **/
    private Team team;

    public ClaimedTile(Team team, ShapeOrtho3D... shapes){
        super(shapes);
        this.team = team;
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
        if (getHoverBrightness().doubleValue() == 0.0){
            return team.getColor();
        } else {
            return blendColors(team.getColor(), Color.WHITE, getHoverBrightness().doubleValue()*0.25);
        }
    }

    @Override
    public Color getUnitColor() {
        return team.getUnitColor();
    }

    // accessors
    public Team getTeam() {
        return team;
    }
}
