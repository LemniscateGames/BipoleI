package lib.gameplay.clusters;

import lib.Tile;
import lib.Unit;
import lib.units.EmptyLand;

public class MoveFilter implements TileFilter {
    private final Unit movingUnit;

    public MoveFilter(Unit movingUnit) {
        this.movingUnit = movingUnit;
    }

    @Override
    public boolean test(Tile tile) {
        return tile == movingUnit || (tile instanceof EmptyLand && ((EmptyLand) tile).getTeam() == movingUnit.getTeam());
    }

    @Override
    public boolean excludeRootTile() {
        return true;
    }
}
