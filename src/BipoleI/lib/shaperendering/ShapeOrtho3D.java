package BipoleI.lib.shaperendering;

import BipoleI.lib.DoublePoint;
import BipoleI.lib.IntPoint;
import BipoleI.lib.Unit;
import BipoleI.lib.battlepanel.BattlePanel;

import java.awt.*;

public abstract class ShapeOrtho3D {
    public abstract Point3D[] generatePoints();
    public abstract Segment3D[] generateSegments();
    public abstract Face3D[] generateFaces();

    private final Point3D[] points;
    private final Segment3D[] segments;
    private final Face3D[] faces;

    private final BattlePanel panel;
    private final Unit unit;

    public ShapeOrtho3D(BattlePanel panel, Unit unit){
        this.panel = panel;
        this.unit = unit;

        points = generatePoints();
        segments = generateSegments();
        faces = generateFaces();
    }

    public void draw(Graphics g, double x, double y){
        // Center shape on center of tile
        double zoom = panel.getZoom();
        DoublePoint pos = panel.tileScreenPos(x, y - zoom/2);

        // Draw faces
        g.setColor(unit.getTeam().getUnitColor());
        for (Face3D face : faces){
            int[] xPoints = new int[face.points.length];
            int[] yPoints = new int[face.points.length];
            for (int i=0; i<face.points.length; i++){
                Point3D point = face.points[i];
                xPoints[i] = (int)(pos.x + point.x*zoom - point.y*zoom);
                yPoints[i] = (int)(pos.y + point.x*zoom/2 + point.y*zoom/2 - point.z);
            }
            g.fillPolygon(xPoints, yPoints, face.points.length);
        }

        // Draw segments
        g.setColor(unit.getTeam().getColor());
        for (Segment3D segment : segments){
            g.drawLine(
                    (int)(pos.x + segment.start.x*zoom - segment.start.y*zoom),
                    (int)(pos.y + segment.start.x*zoom/2 + segment.start.y*zoom/2 - segment.start.z),
                    (int)(pos.x + segment.end.x*zoom - segment.end.y*zoom),
                    (int)(pos.y + segment.end.x*zoom/2 + segment.end.y*zoom/2 - segment.end.z)
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
