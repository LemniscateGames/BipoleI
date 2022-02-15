package lib;

public class Map {
    private Tile[][] tiles;

    public Map(int height, int width){
        tiles = new Tile[height][width];
    }

    public void placeUnit(Tile tile, int row, int col){
        tiles[row][col] = tile;
    }


    public int numRows(){
        return tiles.length;
    }

    public int numCols(){
        return tiles[0].length;
    }
}
