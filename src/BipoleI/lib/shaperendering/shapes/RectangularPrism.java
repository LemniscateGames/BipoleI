package BipoleI.lib.shaperendering.shapes;

import BipoleI.lib.Unit;
import BipoleI.lib.battlepanel.BattlePanel;
import BipoleI.lib.shaperendering.Face3D;
import BipoleI.lib.shaperendering.Point3D;
import BipoleI.lib.shaperendering.Segment3D;
import BipoleI.lib.shaperendering.ShapeOrtho3D;

public class RectangularPrism extends ShapeOrtho3D {
    private final double width, length, height;

    public RectangularPrism(BattlePanel panel, Unit unit, double width, double length, double height){
        super(panel, unit);

        this.width = width;
        this.length = length;
        this.height = height;
    }

    @Override
    public Point3D[] generatePoints() {
        double hw = width/2;
        double hl = length/2;

        return ShapeOrtho3D.generatePointsFromArray(new double[][]{
                //outer
                {hw, hl, 0},
                {-hw, hl, 0},
                {-hw, hl, height},
                {-hw, -hl, height},
                {hw, -hl, height},
                {hw, -hl, 0},
                //front corner
                {hw, hl, height},
        });
    }

    @Override
    public Segment3D[] generateSegments() {
        return generateSegmentsFromArrays(new int[][]{
                {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 0},
                {0, 6}, {2, 6}, {4, 6}
        });
    }

    @Override
    public Face3D[] generateFaces() {
        return generateFacesFromArrays(new int[][]{
                {0, 1, 2, 6},
                {2, 3, 4, 6},
                {4, 5, 0, 6},
        });
    }
}
