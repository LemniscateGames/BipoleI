package lib.units;

import lib.Team;
import lib.Unit;
import lib.display.shaperendering.shapes.RectangularPrism;

import java.awt.*;

public class Soldier extends Unit {
    public Soldier(Team team) {
        super(team, 5, 5, 2, 7500);

        addShape(new RectangularPrism(0.3, 0.3, 0.32));
    }
}
