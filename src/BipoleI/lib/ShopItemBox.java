package BipoleI.lib;

import BipoleI.lib.battlepanel.BattlePanel;

import java.awt.*;
import java.util.ArrayList;

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
