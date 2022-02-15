package lib;

public interface Tile {
    /** Whether or not this tile has a colored border and a white grd filler line should not be draw adjacent to it for empty tiles. **/
    boolean hasBorder();
}
