package lib.display.shaperendering.shapes;

import lib.display.shaperendering.Face3D;
import lib.display.shaperendering.Point3D;
import lib.display.shaperendering.Segment3D;
import lib.display.shaperendering.ShapeOrtho3D;

/** A single segment as a shape.
 * Its position is its start and its end field is its endpoint.
 */
public class SegmentShape extends ShapeOrtho3D {
    private final Point3D end;

    public SegmentShape(double r, double c, double h, double r2, double c2, double h2) {
        super(new Point3D(r, c, h));
        end = new Point3D(r2, c2, h2);
        generateShape();
    }

    @Override
    public Point3D[] generatePoints() {
//        return new Point3D[]{getPosition(), end};
        return new Point3D[0];
    }

    @Override
    public Segment3D[] generateSegments() {
        return new Segment3D[]{new Segment3D(getPosition(), end)};
    }

    @Override
    public Face3D[] generateFaces() {
        return new Face3D[0];
    }
}
