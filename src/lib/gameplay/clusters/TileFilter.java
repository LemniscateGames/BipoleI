package lib.gameplay.clusters;

import lib.Tile;

public interface TileFilter {
    /** Check if the file fits within this cluster. **/
    boolean test(Tile tile);

    /** Whether or not to exclude the root tile of the cluster from the final result. **/
    boolean excludeRootTile();
}
