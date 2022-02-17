package lib;

import lib.display.BattlePanel;

import java.awt.*;

public class ClaimedTile implements Tile {
    private Team team;

    public ClaimedTile(Team team){
        this.team = team;
    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {

    }

    @Override
    public void drawTileBase(Graphics g, double x, double y, double z) {
        BattlePanel.drawTile(g, x, y, z, team.getColor(), team.getTileFillColor());
    }
}
