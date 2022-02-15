package old.BipoleI.lib.units;

import old.BipoleI.lib.Map;
import old.BipoleI.lib.Team;
import old.BipoleI.lib.Unit;
import old.BipoleI.lib.shaperendering.ShapeOrtho3D;
import old.BipoleI.lib.shaperendering.shapes.RectangularPrism;

public class Soldier extends Unit {
    public String name() { return "Soldier"; }

    public Soldier(Map map, Team team) {
        super(map, team,3, 5, 2, 7500, true, false);
        setShapes(new ShapeOrtho3D[]{
                new RectangularPrism(this, 0, 0, 0.4, 0.4, 0.4)
        });
    }
}
