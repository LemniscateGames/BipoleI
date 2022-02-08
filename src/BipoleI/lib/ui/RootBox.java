package BipoleI.lib.ui;

import BipoleI.lib.Alignment;
import BipoleI.lib.ui.ElementBox;

import java.awt.*;

/** An ElementBox except with a fixed position instead of dependent on parents.
 * The root ElementBox must be one of these (or another type of ElementBox which does not rely on parent). **/
public class RootBox extends ElementBox {
    private final int x;
    private final int y;

    public RootBox(int x, int y){
        super(null);
        this.x = x;
        this.y = y;
        setWidth(960);
        setHeight(540);
    }
    public RootBox(){
        this(0, 0);
    }

    /** Root method to draw, takes in height and width because this root may have been resized since last draw. **/
    public void drawAsRoot(Graphics g, int width, int height){
        setWidth(width - getLeftPad() - getRightPad());
        setHeight(height - getTopPad() - getBottomPad());

        for (ElementBox child : getChildren()) {
            child.draw(g);
        }
    }

    // ACCESSORS
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getTotalWidth() {
        return getWidth() + getLeftPad() + getRightPad();
    }

    @Override
    public int getTotalHeight() {
        return getHeight() + getTopPad() + getBottomPad();
    }

    @Override
    public int getTotalContainedWidth() {
        return 0;
    }

    @Override
    public int getTotalContainedHeight() {
        return 0;
    }

    /** In case an ElementBox instantiated as an ElementBox is a child of this, getAlignment() is defined as TOP_LEFT
     * as there is no parent for this to derive an alignment from, so it would error as parent is null. **/
    @Override
    public Alignment getAlignment(){
        return Alignment.TOP_LEFT;
    }
}
