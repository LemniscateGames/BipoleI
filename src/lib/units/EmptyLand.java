package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.RectangularPrism;

public class EmptyLand extends Unit {
    public EmptyLand(Team team) {
        super(team);
    }

    @Override
    public Unit newUnit(Team team) {
        Unit unit = new EmptyLand(team);
        unit.initialize();
        return unit;
    }

    @Override
    public void initialize() {
        setValue(0);
        setHp(1);
        setAtk(0);

        setCanAttack(false);
    }
}
