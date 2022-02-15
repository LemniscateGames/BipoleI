package old.BipoleI.lib.shop;

import old.BipoleI.lib.Team;
import old.BipoleI.lib.units.Castle;
import old.BipoleI.lib.units.Farmer;
import old.BipoleI.lib.units.Soldier;

import java.util.ArrayList;

public class Shop {
    private final ArrayList<Buyable> items;

    public Shop() {
        this.items = new ArrayList<>();
    }

    public void addItem(Buyable item){
        items.add(item);
    }

    public ArrayList<Buyable> getItems() {
        return items;
    }

    public static Shop generateShop(Team team){
        Shop shop = new Shop();

        shop.addItem(new ShopUnit(new Soldier(null, team), 3));
        shop.addItem(new ShopUnit(new Farmer(null, team), 5));
        shop.addItem(new ShopUnit(new Castle(null, team), 1000));

        return shop;
    }
}
