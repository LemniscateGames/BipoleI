package lib;

public class Map {
    private final Tile[][] tiles;

    public Map(int rows, int cols){
        tiles = new Tile[rows][cols];
    }

    public void placeTile(Tile tile, int row, int col){
        tiles[row][col] = tile;
        tile.onPlace(this, row, col);
        System.out.printf("placed %s at %d, %d%n", tile, row, col);
    }

    public Tile getTile(int row, int col){
        if (isOutOfBounds(row, col)) return null;
        return tiles[row][col];
    }

    public boolean isOutOfBounds(int row, int col){
        return row < 0 || row >= numRows() || col < 0 || col >= numCols();
    }

    /** Return true if there is NOT a bordered tile at the given index.= (or index is out of bounds). Used for map rendering time saving. **/
    public boolean noBorderedTile(int row, int col){
        if (isOutOfBounds(row, col)) return true;
        Tile tile = getTile(row, col);
        if (tile == null) return true;
        return !tile.hasBorder();
    }

    /** Return true if there is an adjacent owned tile to the given coordinates. **/
    public boolean hasAdjacentOwnedTile(Team team, int row, int col){
        return isOwnedTile(team, row+1, col)
                || isOwnedTile(team, row-1, col)
                || isOwnedTile(team, row, col+1)
                || isOwnedTile(team, row, col-1);
    }

    /** Returns true if the given coordinates contains a tile owned by the passed the team. **/
    public boolean isOwnedTile(Team team, int row, int col){
        Tile tile = getTile(row, col);
        if (tile instanceof ClaimedTile){
            return ((ClaimedTile) tile).getTeam() == team;
        } else {
            return false;
        }
    }

    public int numRows(){
        return tiles.length;
    }

    public int numCols(){
        return tiles[0].length;
    }
}
