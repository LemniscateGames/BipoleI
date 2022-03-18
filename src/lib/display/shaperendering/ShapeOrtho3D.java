package lib.display.shaperendering;

import lib.gameplay.tiletypes.GeometryTile;

import java.awt.*;

import static lib.panels.BattlePanel.*;

public abstract class ShapeOrtho3D {
    public abstract Point3D[] generatePoints();
    public abstract Segment3D[] generateSegments();
    public abstract Face3D[] generateFaces();

    private GeometryTile linkedTile;
    private final Point3D position;
    private Point3D[] points;
    private Segment3D[] segments;
    private Face3D[] faces;

    public ShapeOrtho3D(Point3D position){
        this.position = position;

        // subclasses should call generateShapes after initializing all its fields
    }

    public void generateShape(){
        points = generatePoints();
        segments = generateSegments();
        faces = generateFaces();
    }

    public void linkTile(GeometryTile tile){
        linkedTile = tile;
    }

    public void draw(Graphics g, double x, double y, double z){
        x += ((position.r+0.5)*ROW_X_OFFSET + (position.c+0.5)*COL_X_OFFSET)*z;
        y += ((position.r+0.5)*ROW_Y_OFFSET + (position.c+0.5)*COL_Y_OFFSET + position.h*HEIGHT_Y_OFFSET)*z;

        // Draw faces
        g.setColor(linkedTile.getUnitColor());
        for (Face3D face : faces){
            int[] xPoints = new int[face.vertices.length];
            int[] yPoints = new int[face.vertices.length];
            for (int i=0; i<face.vertices.length; i++){
                Point3D vertex = face.vertices[i];
                xPoints[i] = (int)(x + ((vertex.r*ROW_X_OFFSET + vertex.c*COL_X_OFFSET)*z));
                yPoints[i] = (int)(y + ((vertex.r*ROW_Y_OFFSET + vertex.c*COL_Y_OFFSET + vertex.h*HEIGHT_Y_OFFSET)*z));
            }
            g.fillPolygon(xPoints, yPoints, face.vertices.length);
        }

        // Draw segments
        g.setColor(linkedTile.getColor());
        for (Segment3D segment : segments){
            g.drawLine(
                    (int)(x + ((segment.start.r*ROW_X_OFFSET + segment.start.c*COL_X_OFFSET)*z)),
                    (int)(y + ((segment.start.r*ROW_Y_OFFSET + segment.start.c*COL_Y_OFFSET + segment.start.h*HEIGHT_Y_OFFSET)*z)),
                    (int)(x + ((segment.end.r*ROW_X_OFFSET + segment.end.c*COL_X_OFFSET)*z)),
                    (int)(y + ((segment.end.r*ROW_Y_OFFSET + segment.end.c*COL_Y_OFFSET + segment.end.h*HEIGHT_Y_OFFSET)*z))
            );
        }
    }

    public Point3D getPosition() {
        return position;
    }

    // point generation utility
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
