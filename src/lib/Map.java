package lib;

public class Map {
    private Tile[][] tiles;

    public Map(int height, int width){
        tiles = new Tile[height][width];
    }

    public void placeTile(Tile tile, int row, int col){
        tiles[row][col] = tile;
        System.out.printf("placed %s at %d, %d%n", tile, row, col);
    }

    public Tile getTile(int row, int col){
//        if (!hasTile(row, col)) return null;
        return tiles[row][col];
    }

    public boolean hasTile(int row, int col){
        return row >= 0 && row < numRows() && col >= 0 && col < numCols();
    }

    public int numRows(){
        return tiles.length;
    }

    public int numCols(){
        return tiles[0].length;
    }
}
