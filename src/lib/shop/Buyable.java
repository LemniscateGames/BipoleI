package lib.shop;

import java.awt.*;

public interface Buyable {
    /** Get the cost of this item. **/
    int getCost();

    /** Must be drawable for the shop UI, so implement a draw method with g,x,y,z params. **/
    void draw(Graphics g, double x, double y, double z);
}
