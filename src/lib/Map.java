package lib;

import lib.gameplay.clusters.MoveFilter;
import lib.gameplay.clusters.TileCluster;
import lib.gameplay.tiletypes.ClaimedTile;
import lib.gameplay.tiletypes.ResponsiveTile;
import lib.misc.RowColPoint;

public class Map {
    private final Tile[][] tiles;
    private Unit movingUnit;
    private int movingRow, movingCol;

    public Map(int rows, int cols){
        tiles = new Tile[rows][cols];
    }

    public void placeTile(Tile tile, int row, int col){
        tiles[row][col] = tile;
        if (tile != null) tile.onPlace(this, row, col);
        System.out.printf("placed %s at %d, %d%n", tile, row, col);
        if (movingUnit != null) recalculateDim();
    }

    public Tile getTile(int row, int col){
        if (isOutOfBounds(row, col)) return null;
        return tiles[row][col];
    }
    public Tile getTile(RowColPoint point){
        return getTile(point.row, point.col);
    }

    public boolean isOutOfBounds(int row, int col){
        return row < 0 || row >= numRows() || col < 0 || col >= numCols();
    }

    /** Set up auto-recalculating the dimness for the moving unit. Returns the TileCluster. **/
    public TileCluster setMovingUnit(Unit unit, int row, int col, boolean excludeMoveRoot){
        movingUnit = unit;
        movingRow = row;
        movingCol = col;
        return recalculateDim(excludeMoveRoot);
    }

    public TileCluster setMovingUnit(Unit unit, int row, int col){
        return setMovingUnit(unit, row, col, true);
    }

    public void clearMovingUnit(){
        movingUnit = null;
        for (int r=0; r<numRows(); r++){
            for (int c=0; c<numCols(); c++){
                Tile tile = getTile(r, c);
                if (tile instanceof ResponsiveTile){
                    ResponsiveTile respTile = ((ResponsiveTile) tile);
                    respTile.setDimness(0);
                    respTile.setBaseDrawPriority(false);
                }
            }
        }
    }

    /** Returns a new TileCluster to recalculate move area and dims tiles not within that area. **/
    public TileCluster recalculateDim(boolean excludeMoveRoot){
        if (movingUnit == null) return null;
        Tile movingTile = getTile(movingRow, movingCol);
        if (!(movingTile instanceof Unit)) return null;
        Unit movingUnit = (Unit) getTile(movingRow, movingCol);
        TileCluster undimmedTiles = TileCluster.generateCluster(
                new MoveFilter(movingUnit, excludeMoveRoot), this, movingUnit.getSpeed(), movingRow, movingCol);

        for (int r=0; r<numRows(); r++){
            for (int c=0; c<numCols(); c++){
                Tile tile = getTile(r, c);
                if (tile instanceof ResponsiveTile){
                    ResponsiveTile respTile = (ResponsiveTile) tile;
                    if (undimmedTiles.containsPoint(r, c) || (r == movingRow && c == movingCol)){
                        respTile.setDimness(0);
                        respTile.setBaseDrawPriority(true);
                    } else {
                        respTile.setDimness(0.5);
                        respTile.setBaseDrawPriority(false);
                    }
                }
            }
        }

        movingUnit.setDimness(-0.25);
        return undimmedTiles;
    }

    public TileCluster recalculateDim(){
        return recalculateDim(true);
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
