package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.TriangularPrism;

public class Farmer extends Unit {
    public Farmer(Team team) {
        super(team);
    }

    @Override
    public String name() {
        return "Farmer";
    }

    @Override
    public String desc() {
        return "Generates points over time. Cannot attack or move.";
    }

    @Override
    public void initialize() {
        setValue(4);
        setHp(1);
        setAtk(0);
        setDelay(20000);

        setActable(false);
        setAutoAct(true);

        addShape(new TriangularPrism(0.3, 0.3, 0.32));
    }

    public void autoAct() {
        generatePoints(1);
    }

    @Override
    public Unit newUnit(Team team) {
        Unit unit = new Farmer(team);
        unit.initialize();
        return unit;
    }
}

//    public Farmer(Team team) {
//        super(team, 5, 5, 2, 10000);
//        setCanAttack(false);
//        setAutoAct(true);
//
//        addShape(new TriangularPrism(0.3, 0.3, 0.32));
//    }
