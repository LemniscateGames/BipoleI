package lib.ui;

import lib.panels.BattlePanel;
import lib.panels.ElementPanel;
import lib.shop.Shop;
import lib.shop.ShopItem;

import javax.swing.*;
import java.awt.*;

public class ShopItemElementBox extends ElementBox {
    public static final Color CANNOT_AFFORD_COLOR = new Color(160, 144, 144);

    private Shop shop;
    private ShopItem item;
    private ElementBox costText;
    private boolean canBuy;

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
    public void beforeDraw() {
        canBuy = shop.getTeam().canBuy(item);
        setBorder(canBuy ? UI_FG_COLOR : UI_BORDER_COLOR);
        costText.setFg(canBuy ? UI_FG_COLOR : CANNOT_AFFORD_COLOR);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        item.getItem().draw(g,
                xPos() + getWidth()/2.0,
                yPos() + 12,
                getWidth()*0.8,
                !canBuy);
    }
}
