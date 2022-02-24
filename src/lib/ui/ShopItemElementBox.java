package lib.ui;

import lib.panels.BattlePanel;
import lib.panels.ElementPanel;
import lib.shop.Shop;
import lib.shop.ShopItem;

import javax.swing.*;
import java.awt.*;

public class ShopItemElementBox extends ElementBox {
    private Shop shop;
    private ShopItem item;
    private ElementBox costText;

    public ShopItemElementBox(Shop shop, ShopItem item, ElementBox shopElement){
        this.shop = shop;
        this.item = item;

        costText = new ElementBox(0, 0, Shop.SHOP_ITEM_WIDTH, Shop.SHOP_ITEM_TEXT_SPACE);
        costText.setRelBottomElement(this);
        costText.setBottom(2);
        costText.setRelLeftElement(this);

        costText.setxAlign(Alignment.START);
        costText.setyAlign(Alignment.END);

        costText.setTransparentBg(true);
        costText.setFont(ElementBox.GAME_FONT_SMALL);
        costText.setText(item.getCost()+" pts");
    }

    @Override
    public void initialize(ElementPanel panel) {
        super.initialize(panel);
        panel.addElement(costText);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        item.getItem().draw(g,
                xPos() + getWidth()/2.0,
                yPos() + 12,
                getWidth()*0.8);
    }
}
