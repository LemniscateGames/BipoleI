package lib;

import lib.display.AnimatedValue;
import lib.panels.BattlePanel;

import java.awt.*;

import static lib.display.ColorUtils.blendColors;

public class ClaimedTile implements Tile {
    /** The team that claimed this tile. **/
    private Team team;

    /** The number that controls the brightness of this tile when hovered over. Set to an AnimatedValue when onHover() is called. **/
    private Number hoverBrightness;

    public ClaimedTile(Team team){
        this.team = team;

        hoverBrightness = 0.0;
    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {

    }

    @Override
    public void onHover() {
        if (BattlePanel.EASE_CURSOR) {
            hoverBrightness = new AnimatedValue(BattlePanel.CURSOR_SPEED, hoverBrightness.doubleValue(), 1.0);
        } else {
            hoverBrightness = 1.0;
        }
    }

    @Override
    public void onUnhover() {
        if (BattlePanel.EASE_CURSOR) {
            hoverBrightness = new AnimatedValue(BattlePanel.CURSOR_SPEED, hoverBrightness.doubleValue(), 0.0);
        } else {
            hoverBrightness = 0.0;
        }
    }

    @Override
    public void drawTileBase(Graphics g, double x, double y, double z) {
        Color teamColor;
        if (hoverBrightness.doubleValue() == 0.0){
            teamColor = team.getColor();
        } else {
            teamColor = blendColors(team.getColor(), Color.WHITE, hoverBrightness.doubleValue()*0.25);
        }

        BattlePanel.drawTile(g, x, y, z, teamColor, team.getTileFillColor());
    }

    @Override
    public boolean hasBorder() {
        return true;
    }
}
