package BipoleI.lib.shop;

import BipoleI.lib.Unit;
import BipoleI.lib.shop.Buyable;

public class ShopUnit implements Buyable {
    private final Unit unit;
    private final int cost;

    public ShopUnit(Unit unit, int cost){
        this.unit = unit;
        this.cost = cost;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getCost() {
        return cost;
    }
}
