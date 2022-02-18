package lib;

public class Map {
    private Tile[][] tiles;

    public Map(int rows, int cols){
        tiles = new Tile[rows][cols];
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

    /** Return true if there is NOT a bordered tile at the given index.= (or index is out of bounds). Used for map rendering time saving. **/
    public boolean noBorderedTile(int row, int col){
        if (!hasTile(row, col)) return true;
        Tile tile = getTile(row, col);
        if (tile == null) return true;
        return !tile.hasBorder();
    }

    public int numRows(){
        return tiles.length;
    }

    public int numCols(){
        return tiles[0].length;
    }
}
