package old.BipoleI.lib.shaperendering.shapes;

import old.BipoleI.lib.Unit;
import old.BipoleI.lib.shaperendering.Face3D;
import old.BipoleI.lib.shaperendering.Point3D;
import old.BipoleI.lib.shaperendering.Segment3D;
import old.BipoleI.lib.shaperendering.ShapeOrtho3D;

public class TriangularPrism extends ShapeOrtho3D {
    private final double width, length, height;

    public TriangularPrism(Unit unit, double x, double y, double width, double length, double height){
        super(unit, x, y);

        this.width = width;
        this.length = length;
        this.height = height;
    }

    @Override
    public Point3D[] generatePoints() {
        double hw = width/2;
        double hl = length/2;

        return ShapeOrtho3D.generatePointsFromArray(new double[][]{
                {hw, hl, 0},
                {-hw, hl, 0},
                {0, 0, height},
                {hw, -hl, 0},
        });
    }

    @Override
    public Segment3D[] generateSegments() {
        return generateSegmentsFromArrays(new int[][]{
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {0, 2}
        });
    }

    @Override
    public Face3D[] generateFaces() {
        return generateFacesFromArrays(new int[][]{
                {0, 1, 2}, {2, 3, 0}
        });
    }
}
