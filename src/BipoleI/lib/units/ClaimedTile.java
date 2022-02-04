package BipoleI.lib.units;

import BipoleI.lib.Map;
import BipoleI.lib.Team;
import BipoleI.lib.Unit;

import java.awt.*;

public class ClaimedTile extends Unit {
    public String name() { return "Empty Tile"; }

    public ClaimedTile(Map map, Team team) {
        super(map, team,1, 1, 0, 0, false);
    }

    @Override
    public void draw(Graphics g, int x, int y, int z) {
        // draw literally nothing because this is an empty tile. :)
    }

    public boolean canAct(){
        return false;
    }
}
