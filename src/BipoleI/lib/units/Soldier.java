package BipoleI.lib.units;

import BipoleI.BattlePanel;
import BipoleI.lib.Map;
import BipoleI.lib.Team;
import BipoleI.lib.Unit;

import java.awt.*;

public class Soldier extends Unit {
    public String name() { return "Soldier"; }

    public Soldier(Map map, Team team) {
        super(map, team,3, 5, 2, 7500);
    }

    @Override
    public void draw(Graphics g, int x, int y, int z, boolean brighter) {
        BattlePanel.drawRectPrism(g, x, y, z, getTeam(), (int)(z/3.0), (int)(z/3.0), (int)(z/2.5), brighter);
    }
}
