package lib.gameplay.clusters;

import lib.Map;
import lib.misc.RowColPoint;

import java.util.HashSet;

/** Represents an area of tiles on the map that can be any shape.
 * Used to calculate and display the areas that effects can reach. **/
public class TileCluster {
    private final HashSet<RowColPoint> points;
    private final TileFilter filter;

    public TileCluster(TileFilter filter){
        points = new HashSet<>();
        this.filter = filter;
    }

    public void addPoint(RowColPoint point){
        points.add(point);
    }
    public void addPoint(int row, int col){
        addPoint(new RowColPoint(row, col));
    }

    public void removePoint(RowColPoint point){
        points.remove(point);
    }
    public void removePoint(int row, int col){
        removePoint(new RowColPoint(row, col));
    }

    public boolean containsPoint(int row, int col){
        return points.contains(new RowColPoint(row, col));
    }

    // CLUSTER GENERATION
    public static TileCluster generateCluster(TileFilter filter, Map map, int range, int row, int col){
        TileCluster cluster = new TileCluster(filter);

        cluster.branchOut(map, range, row, col);
        if (filter.excludeRootTile()) cluster.removePoint(row, col);

        return cluster;
    }

    private void branchOut(Map map, int range, int row, int col){
        if (range > 0 && filter.test(map.getTile(row, col))) {
            range -= 1;
            addPoint(row, col);
            branchOut(map, range, row-1, col);
            branchOut(map, range, row+1, col);
            branchOut(map, range, row, col-1);
            branchOut(map, range, row, col+1);
        }
    }
}
