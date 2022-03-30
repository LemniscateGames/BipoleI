package lib.gameplay.clusters;

import lib.Tile;
import lib.Unit;
import lib.units.EmptyLand;

public class MoveFilter implements TileFilter {
    private final Unit movingUnit;
    private final boolean excludeRoot;

    public MoveFilter(Unit movingUnit, boolean excludeRoot) {
        this.movingUnit = movingUnit;
        this.excludeRoot = excludeRoot;
    }
    public MoveFilter(Unit movingUnit){
        this(movingUnit, true);
    }

    @Override
    public boolean test(Tile tile) {
        if (movingUnit.isClaimedLandOnly()){
            return tile == movingUnit || (tile instanceof EmptyLand && ((EmptyLand) tile).getTeam() == movingUnit.getTeam());
        } else {
            return tile == movingUnit || tile instanceof EmptyLand || !(tile instanceof Unit);
        }
    }

    @Override
    public boolean excludeRootTile() {
        return excludeRoot;
    }
}
