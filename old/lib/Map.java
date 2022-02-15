package old.BipoleI.lib;

import old.BipoleI.lib.battlepanel.BattlePanel;

public class Map {
    private final Tile[][] tiles;

    /** (Optional) Panel that this map is currently displaying to. **/
    private BattlePanel panel;

    public Map(int height, int width){
        tiles = new Tile[height][width];
        for (int r=0; r<height; r++){
            for (int c=0; c<width; c++){
                tiles[r][c] = new UnclaimedTile(this);
            }
        }
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
        if (c>=numCols() || c<0 || r>=numRows() || r<0) return null;
        return tiles[c][r];
    }

    public void placeUnit(int c, int r, Unit unit){
        tiles[c][r] = unit;
        unit.place();
        unit.setPanel(panel);
    }

    public IntPoint getPositionOf(Tile tile){
        for (int c=0; c<numCols(); c++){
            for (int r=0; r<numRows(); r++){
                if (tiles[c][r] == tile) {
                    return new IntPoint(c, r);
                }
            }
        }
        return null;
    }

    public void replaceTile(Tile tile, Tile newTile){
        for (int c=0; c<numCols(); c++){
            for (int r=0; r<numRows(); r++){
                if (tiles[c][r] == tile) {
                    tiles[c][r] = newTile;
                }
            }
        }
    }

    public boolean isAdjacentOwnedTile(int c, int r, Team team){
        if (isTileTeam(c+1, r, team)) return true;
        if (isTileTeam(c-1, r, team)) return true;
        if (isTileTeam(c, r+1, team)) return true;
        if (isTileTeam(c, r-1, team)) return true;
        return false;
    }
    public boolean isAdjacentOwnedTile(Tile tile, Team team){
        IntPoint pos = getPositionOf(tile);
        return isAdjacentOwnedTile(pos.getX(), pos.getY(), team);
    }

    public boolean isTileTeam(Tile tile, Team team){
        if (tile instanceof Unit){
            return ((Unit) tile).getTeam() == team;
        } else {
            return false;
        }
    }
    public boolean isTileTeam(int c, int r, Team team){
        return isTileTeam(getTile(c, r), team);
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
