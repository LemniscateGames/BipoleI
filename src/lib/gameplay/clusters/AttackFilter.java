package lib.gameplay.clusters;

import lib.Tile;
import lib.Unit;

public class AttackFilter implements TileFilter {
    private final Unit attackingUnit;

    public AttackFilter(Unit attackingUnit) {
        this.attackingUnit = attackingUnit;
    }

    @Override
    public boolean test(Tile tile) {
//        return attackingUnit.isCanAttack() && (tile == null || tile == attackingUnit || (tile instanceof Unit && ((Unit) tile).getTeam() != attackingUnit.getTeam()));
        return attackingUnit.isActable();

    }

    @Override
    public boolean excludeRootTile() {
        return true;
    }
}
