package old.BipoleI.lib.shop;

import old.BipoleI.lib.Unit;

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
