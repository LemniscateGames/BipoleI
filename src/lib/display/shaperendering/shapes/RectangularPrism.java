package lib.display.shaperendering.shapes;

import lib.ResponsiveTile;
import lib.display.shaperendering.Face3D;
import lib.display.shaperendering.Point3D;
import lib.display.shaperendering.Segment3D;
import lib.display.shaperendering.ShapeOrtho3D;

public class RectangularPrism extends ShapeOrtho3D {
    private final double width, length, height;
    private final boolean noTop, noBottom;

    public RectangularPrism(double r, double c, double h, double width, double length, double height, boolean noTop, boolean noBottom) {
        super(new Point3D(r, c, h));

        this.width = width;
        this.length = length;
        this.height = height;
        this.noTop = noTop;
        this.noBottom = noBottom;

        generateShape();
    }
    public RectangularPrism(double r, double c, double h, double width, double length, double height){
        this(r, c, h, width, length, height, false, false);
    }
    public RectangularPrism(double width, double length, double height){
        this(0,0,0, width, length, height);
    }

    @Override
    public Point3D[] generatePoints() {
        double hw = width/2.0;
        double hl = length/2.0;

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
        if (noTop && noBottom){
            return generateSegmentsFromArrays(new int[][]{
                            {1, 2},                 {4, 5},
                    {0, 6}
            });
        } else if (noTop){
            return generateSegmentsFromArrays(new int[][]{
                    {0, 1}, {1, 2},                 {4, 5}, {5, 0},
                    {0, 6},
            });
        } else if (noBottom) {
            return generateSegmentsFromArrays(new int[][]{
                            {1, 2}, {2, 3}, {3, 4}, {4, 5},
                    {0, 6}, {2, 6}, {4, 6}
            });
        } else {
            return generateSegmentsFromArrays(new int[][]{
                    {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 0},
                    {0, 6}, {2, 6}, {4, 6}
            });
        }
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
