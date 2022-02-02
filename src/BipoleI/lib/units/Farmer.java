package BipoleI.lib.units;

import BipoleI.BattlePanel;
import BipoleI.lib.Map;
import BipoleI.lib.Team;
import BipoleI.lib.Unit;

import java.awt.*;

public class Farmer extends Unit {
    public String name() { return "Farmer"; }

    public Farmer(Map map, Team team) {
        super(map, team,5, 3, 0, 10000, false, true);
    }

    @Override
    public void autoAct() {
        generatePoints(1);
    }

    @Override
    public void draw(Graphics g, int x, int y, int z, boolean brighter) {
        BattlePanel.drawTriangularPrism(g, x, y, z, getTeam(), (int)(z/3.0), (int)(z/3.0), (int)(z/2.5), brighter);
    }
}
