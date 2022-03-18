package lib.display.shaperendering.shapes;

import lib.display.shaperendering.Face3D;
import lib.display.shaperendering.Point3D;
import lib.display.shaperendering.Segment3D;
import lib.display.shaperendering.ShapeOrtho3D;

public class TriangularPrism extends ShapeOrtho3D {
    private final double width, length, height;

    public TriangularPrism(double r, double c, double h, double width, double length, double height) {
        super(new Point3D(r, c, h));

        this.width = width;
        this.length = length;
        this.height = height;

        generateShape();
    }
    public TriangularPrism(double width, double length, double height){
        this(0,0,0, width, length, height);
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
                {0, 1}, {1, 2}, {2, 3}, {3, 0}, // outer
                {0, 2} // center
        });
    }

    @Override
    public Face3D[] generateFaces() {
        return generateFacesFromArrays(new int[][]{
                {0, 1, 2}, {2, 3, 0}
        });
    }
}
