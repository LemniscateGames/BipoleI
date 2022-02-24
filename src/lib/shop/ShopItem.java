package lib.shop;

public class ShopItem {
    private Buyable item;
    private int cost;

    public ShopItem(Buyable item, int cost){
        this.item = item;
        this.cost = cost;
    }

    public Buyable getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }
}
