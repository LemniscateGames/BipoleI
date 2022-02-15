package old.BipoleI.lib.shaperendering;

import old.BipoleI.lib.Unit;

import java.awt.*;

public abstract class ShapeOrtho3D {
    public abstract Point3D[] generatePoints();
    public abstract Segment3D[] generateSegments();
    public abstract Face3D[] generateFaces();

    private final Point3D[] points;
    private final Segment3D[] segments;
    private final Face3D[] faces;

    private final Unit unit;

    private double x;
    private double y;

    public ShapeOrtho3D(Unit unit, double x, double y){
        this.unit = unit;
        this.x = x;
        this.y = y;

        points = generatePoints();
        segments = generateSegments();
        faces = generateFaces();
    }

    public void draw(Graphics g, double x, double y, double z){
        // Center shape on center of tile
        double posX = this.x + x*z;
        double posY = this.y + y*z - z/2;

        // Draw faces
        g.setColor(unit.getUnitColor());
        for (Face3D face : faces){
            int[] xPoints = new int[face.points.length];
            int[] yPoints = new int[face.points.length];
            for (int i=0; i<face.points.length; i++){
                Point3D point = face.points[i];
                xPoints[i] = (int)(posX + point.x*z - point.y*z);
                yPoints[i] = (int)(posY + point.x*z/2 + point.y*z/2 - point.z);
            }
            g.fillPolygon(xPoints, yPoints, face.points.length);
        }

        // Draw segments
        g.setColor(unit.getColor());
        for (Segment3D segment : segments){
            g.drawLine(
                    (int)(posX + segment.start.x*z - segment.start.y*z),
                    (int)(posY + segment.start.x*z/2 + segment.start.y*z/2 - segment.start.z),
                    (int)(posX + segment.end.x*z - segment.end.y*z),
                    (int)(posY + segment.end.x*z/2 + segment.end.y*z/2 - segment.end.z)
            );
        }
    }

    public static Point3D[] generatePointsFromArray(double[][] pointArrs){
        Point3D[] points = new Point3D[pointArrs.length];
        for (int i=0; i<points.length; i++){
            double[] row = pointArrs[i];
            points[i] = new Point3D(row[0], row[1], row[2]);
        }
        return points;
    }

    public Segment3D[] generateSegmentsFromArrays(int[]... segmentIndexArrs){
        Segment3D[] segments = new Segment3D[segmentIndexArrs.length];
        for (int i=0; i<segments.length; i++){
            int[] indices = segmentIndexArrs[i];
            segments[i] = new Segment3D(points[indices[0]], points[indices[1]]);
        }
        return segments;
    }

    public Face3D[] generateFacesFromArrays(int[]... faceIndexArrs){
        Face3D[] faces = new Face3D[faceIndexArrs.length];
        for (int i=0; i<faces.length; i++){
            int[] indices = faceIndexArrs[i];
            Point3D[] facePoints = new Point3D[indices.length];
            for (int j=0; j<facePoints.length; j++){
                facePoints[j] = points[indices[j]];
            }
            faces[i] = new Face3D(facePoints);
        }
        return faces;
    }
}
