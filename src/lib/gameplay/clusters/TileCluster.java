package lib.gameplay.clusters;

import lib.Map;
import lib.misc.RowColPoint;

import java.awt.*;
import java.util.HashSet;

/** Represents an area of tiles on the map that can be any shape.
 * Used to calculate and display the areas that effects can reach. **/
public class TileCluster {
    private final Map map;
    private final int rootRow, rootCol;
    private final HashSet<RowColPoint> points;
    private final TileFilter filter;

    public TileCluster(Map map, TileFilter filter, int row, int col){
        this.map = map;
        rootRow = row;
        rootCol = col;
        points = new HashSet<>();
        this.filter = filter;
    }
    public TileCluster(TileCluster cluster){
        map = cluster.map;
        rootRow = cluster.rootRow;
        rootCol = cluster.rootCol;
        points = new HashSet<>(cluster.points);
        filter = cluster.filter;
    }

    // INTERACTION
    public boolean addPoint(RowColPoint point){
        return points.add(point);
    }
    public boolean addPoint(int row, int col){
        return addPoint(new RowColPoint(row, col));
    }

    public void removePoint(RowColPoint point){
        points.remove(point);
    }
    public void removePoint(int row, int col){
        removePoint(new RowColPoint(row, col));
    }

    public boolean containsPoint(RowColPoint point){
        return points.contains(point);
    }
    public boolean containsPoint(int row, int col){
        return containsPoint(new RowColPoint(row, col));
    }

    // OPERATIONS
    public void add(TileCluster cluster){
        for (RowColPoint point : cluster.points){
            addPoint(point);
        }
    }
    public void subtract(TileCluster cluster){
        for (RowColPoint point : cluster.points){
            removePoint(point);
        }
    }

    // CLUSTER GENERATION
    public static TileCluster generateCluster(TileFilter filter, Map map, int range, int row, int col){
        TileCluster cluster = new TileCluster(map, filter, row, col);

        cluster.branchOut(range, row, col);
        if (filter.excludeRootTile()) cluster.removePoint(row, col);

        return cluster;
    }

    private void branchOut(int range, int row, int col){
        if (range >= 0 && row >= 0 && row < map.numRows() && col >= 0 && col < map.numCols()) {
            RowColPoint point = new RowColPoint(row, col);
            if (filter.test(map.getTile(point))){
                range -= 1;
                addPoint(row, col);
                branchOut(range, row-1, col);
                branchOut(range, row+1, col);
                branchOut(range, row, col-1);
                branchOut(range, row, col+1);
            }
        }
    }

    public TileCluster extend(TileFilter filter, int range){
        long startTime = System.currentTimeMillis();

        TileCluster extendedCluster = new TileCluster(this);
        for (RowColPoint point : points){
            TileCluster extension = generateCluster(filter, map, range, point.row, point.col);
            extendedCluster.add(extension);
        }

        System.out.printf("extended in %dms%n", System.currentTimeMillis()-startTime);

        return extendedCluster;
    }

    // ACCESSORS
    public HashSet<RowColPoint> getPoints() {
        return points;
    }
}
