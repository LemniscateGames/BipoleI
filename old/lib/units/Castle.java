package old.BipoleI.lib.units;

import old.BipoleI.lib.Map;
import old.BipoleI.lib.Team;
import old.BipoleI.lib.Unit;
import old.BipoleI.lib.shaperendering.ShapeOrtho3D;
import old.BipoleI.lib.shaperendering.shapes.RectangularPrism;
import old.BipoleI.lib.shaperendering.shapes.TriangularPrism;

import java.util.ArrayList;

/** The player's main unit. Immovable, and if destroyed the player loses. **/
public class Castle extends Unit {
    public String name() { return "Castle"; }

    public Castle(Map map, Team team) {
        super(map, team,0, 25, 0, 5000, false, true);

        final double SIZE = 0.7;
        final double HEIGHT = 0.3;

        final double TOWER_SPACING = 0.5;
        final double TOWER_SIZE = SIZE - TOWER_SPACING;
        final double TOWER_HEIGHT = 0.15;

        final double CENTER_TOWER_SIZE = 0.2;
        final double CENTER_TOWER_HEIGHT = 0.2;
        final double CENTER_TOWER_TRIANGLE_HEIGHT = 0.25;

        ArrayList<ShapeOrtho3D> shapeList = new ArrayList<>();

        // Main body
        shapeList.add(new RectangularPrism(this, 0, 0, SIZE, SIZE, HEIGHT));

        for (int x=-1; x<1; x++){
            for (int y=-1; y<1; y++){
                if (x==0 && y==0){
                    // draw center tower
                    shapeList.add(new RectangularPrism(this,
                            0, HEIGHT, CENTER_TOWER_SIZE, CENTER_TOWER_SIZE, CENTER_TOWER_HEIGHT
                    ));
                    shapeList.add(new TriangularPrism(this,
                            0, HEIGHT+CENTER_TOWER_HEIGHT, CENTER_TOWER_SIZE, CENTER_TOWER_SIZE, CENTER_TOWER_TRIANGLE_HEIGHT
                    ));

                } else {
                    // draw side towers
                    shapeList.add(new RectangularPrism(this,
                            TOWER_SPACING*x, TOWER_SPACING*y + HEIGHT, TOWER_SIZE, TOWER_SIZE, TOWER_HEIGHT
                    ));

                }
            }
        }

        setShapes(shapeList);
    }

    @Override
    public void autoAct() {
        generatePoints(1);
    }
}
