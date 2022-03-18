package lib.gameplay.tiletypes;

import lib.display.shaperendering.ShapeOrtho3D;
import lib.gameplay.tiletypes.ResponsiveTile;

import java.awt.*;
import java.util.ArrayList;

/** A responsive tile that has ShapeOrtho3Ds that it draws for its draw() method. **/
public abstract class GeometryTile extends ResponsiveTile {
    public final ArrayList<ShapeOrtho3D> shapes;

    public GeometryTile(ShapeOrtho3D... shapes){
        super();
        this.shapes = new ArrayList<>();
        for (ShapeOrtho3D shape : shapes){
            addShape(shape);
        }
    }

    public void addShape(ShapeOrtho3D shape){
        shapes.add(shape);
        shape.linkTile(this);
    }

    @Override
    public void draw(Graphics g, double x, double y, double z) {
        for (ShapeOrtho3D shape : shapes){
            shape.draw(g, x, y, z);
        }
    }
}
