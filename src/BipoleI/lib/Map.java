package BipoleI.lib;

import BipoleI.BattlePanel;

import java.awt.*;

public class Map {
    private final Tile[][] tiles;

    /** (Optional) Panel that this map is currently displaying to. **/
    private BattlePanel panel;

    public Map(int height, int width){
        tiles = new Tile[height][width];
        // null = unclaimed tile that is currently uncontested.
    }

    public int numCols(){
        return tiles.length;
    }
    public int numRows(){
        if (tiles.length == 0) return 0;
        return tiles[0].length;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int c, int r){
        return tiles[c][r];
    }

    public void placeUnit(int c, int r, Unit unit){
        tiles[c][r] = unit;
        unit.place();
        unit.setPanel(panel);
    }

    // ================ ACCESSORS & SETTERS

    public void setPanel(BattlePanel panel) {
        this.panel = panel;
        for (Tile[] row : tiles){
            for (Tile tile : row){
                if (tile instanceof Unit){
                    ((Unit) tile).setPanel(panel);
                }
            }
        }
    }
}
