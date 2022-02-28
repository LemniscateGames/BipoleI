package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.RectangularPrism;

import java.awt.*;

public class Soldier extends Unit {
    public Soldier(Team team) {
        super(team);
    }

    @Override
    public void initialize() {
        setValue(2);
        setHp(5);
        setAtk(2);
        setDelay(7500);

        addShape(new RectangularPrism(0.3, 0.3, 0.32));
    }

    @Override
    public Unit newUnit(Team team) {
        Unit unit = new Soldier(team);
        unit.initialize();
        return unit;
    }
}
