package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.RectangularPrism;
import lib.display.shaperendering.shapes.TriangularPrism;

public class Castle extends Unit {
    private static final double SIZE = 0.7;
    private static final double HEIGHT = 0.3;

    private static final double TOWER_SIZE = 0.125;
    private static final double TOWER_SPACING = TOWER_SIZE + (SIZE - TOWER_SIZE*3)/2;
//    private static final double TOWER_SPACE_BETWEEN = (SIZE - TOWER_SIZE*3)/2;
    private static final double TOWER_HEIGHT = 0.125;

    private static final double CENTER_TOWER_SIZE = 0.2;
    private static final double CENTER_TOWER_HEIGHT = 0.2;
    private static final double CENTER_TOWER_TRIANGLE_HEIGHT = 0.25;

    public Castle(Team team){
        super(team);
    }

    @Override
    public String name() {
        return "Castle";
    }

    @Override
    public String desc() {
        return "Generates points over time. Cannot attack or move. If this tile is destroyed, you lose the game.";
    }

    @Override
    public void initialize() {
        setValue(500);
        setHp(25);
        setAtk(5);
        setDelay(4000);

        setSellable(false);
        setActable(false);
        setAutoAct(true);

        // main body
        addShape(new RectangularPrism(0, 0, 0, SIZE, SIZE, HEIGHT, false, false));

        // towers
        for (int r=-1; r<=1; r++){
            for (int c=-1; c<=1; c++){
                if (r==0 && c==0){
                    // center tower
                    addShape(new RectangularPrism(0, 0, HEIGHT,
                            CENTER_TOWER_SIZE, CENTER_TOWER_SIZE, CENTER_TOWER_HEIGHT, true, true));
                    addShape(new TriangularPrism(0, 0, HEIGHT+CENTER_TOWER_HEIGHT,
                            CENTER_TOWER_SIZE, CENTER_TOWER_SIZE, CENTER_TOWER_TRIANGLE_HEIGHT));
                } else {
                    // side towers
                    addShape(new RectangularPrism(TOWER_SPACING*r, TOWER_SPACING*c, HEIGHT,
                            TOWER_SIZE, TOWER_SIZE, TOWER_HEIGHT, false, true));
                }
            }
        }
    }


    @Override
    public void autoAct() {
        generatePoints(1);
    }

    @Override
    public Unit newUnit(Team team) {
        Unit unit = new Castle(team);
        unit.initialize();
        return unit;
    }
}
