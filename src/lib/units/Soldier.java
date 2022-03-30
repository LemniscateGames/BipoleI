package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.RectangularPrism;

public class Soldier extends Unit {
    public Soldier(Team team) {
        super(team);
    }

    @Override
    public String name() {
        return "Soldier";
    }

    @Override
    public String desc() {
        return "A basic attacker.";
    }

    @Override
    public void initialize() {
        setValue(3);
        setHp(5);
        setAtk(3);
        setDelay(7500);
        setSpeed(2);
        setRange(1);

        addShape(new RectangularPrism(0.3, 0.3, 0.32));
    }

    @Override
    public Unit newUnit(Team team) {
        Unit unit = new Soldier(team);
        unit.initialize();
        return unit;
    }
}
