package BipoleI.lib;

public class Map {
    private final Tile[][] tiles;

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

    public Tile getTile(int c, int r){
        return tiles[c][r];
    }

    public void placeUnit(int c, int r, Unit unit){
        tiles[c][r] = unit;
        unit.place();
    }
}
