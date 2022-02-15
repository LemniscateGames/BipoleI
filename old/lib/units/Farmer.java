package old.BipoleI.lib.units;

import old.BipoleI.lib.Map;
import old.BipoleI.lib.Team;
import old.BipoleI.lib.Unit;
import old.BipoleI.lib.shaperendering.ShapeOrtho3D;
import old.BipoleI.lib.shaperendering.shapes.TriangularPrism;

public class Farmer extends Unit {
    public String name() { return "Farmer"; }

    public Farmer(Map map, Team team) {
        super(map, team,5, 3, 0, 10000, false, true);
        setShapes(new ShapeOrtho3D[]{
                new TriangularPrism(this, 0, 0, 0.4, 0.4, 0.4)
        });
    }

    @Override
    public void autoAct() {
        generatePoints(1);
    }
}
