package BipoleI.lib.shop;

import BipoleI.lib.ui.ElementBox;

import java.awt.*;

public class ShopItemBox extends ElementBox {
    private Buyable item;

    public ShopItemBox(ElementBox parent, Buyable item) {
        super(parent);
        this.item = item;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
