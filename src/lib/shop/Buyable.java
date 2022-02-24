package lib.shop;

import java.awt.*;

public interface Buyable {
    /** Get the cost of this item. **/
    int getCost();

    /** Must be drawable for the shop UI, so implement a draw method with g,x,y,z and dim boolean params.
     * If the dim boolean is true then the item is not affordable and should be drawn darker. **/
    void draw(Graphics g, double x, double y, double z, boolean dim);
}
