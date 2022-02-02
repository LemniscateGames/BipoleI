package BipoleI.lib;

import java.awt.*;

public class EmptyTile extends Unit {
    public String name() { return "Empty Tile"; }

    public EmptyTile(Map map, Team team) {
        super(map, team,1, 1, 0, 0, false);
    }

    @Override
    public void draw(Graphics g, int x, int y, int z, boolean brighter) {
        // draw literally nothing because this is an empty tile. :)
    }
}
