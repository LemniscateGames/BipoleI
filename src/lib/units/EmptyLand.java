package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.RectangularPrism;

public class EmptyLand extends Unit {
    public EmptyLand(Team team) {
        super(team, 0, 1, 0, 0);
        setCanAttack(false);
    }
}
