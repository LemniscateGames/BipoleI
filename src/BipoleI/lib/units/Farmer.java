package BipoleI.lib.units;

import BipoleI.lib.battlepanel.BattlePanel;
import BipoleI.lib.Map;
import BipoleI.lib.Team;
import BipoleI.lib.Unit;

import java.awt.*;

public class Farmer extends Unit {
    public String name() { return "Farmer"; }

    public Farmer(Map map, Team team) {
        super(map, team,5, 3, 0, 10000, false, true);
    }
    public Farmer(){}

    @Override
    public void autoAct() {
        generatePoints(1);
    }

    @Override
    public void draw(Graphics g, int x, int y, int z) {
        BattlePanel.drawTriangularPrism(g, x, y, z, getTeam(), (int)(z/3.0), (int)(z/3.0), (int)(z/2.5), getBrightness(), getGrayness());
    }
}
