package lib.ui;

import lib.Team;
import lib.panels.ElementPanel;
import lib.shop.Shop;
import lib.shop.ShopItem;

import java.awt.*;

public class ShopItemElementBox extends ElementBox {
    public static final Color CANNOT_AFFORD_COLOR = new Color(160, 144, 144);

    private Shop shop;
    private ShopItem item;
    private ElementBox costText;
    private boolean canBuy;
    private boolean selected;

    public ShopItemElementBox(Shop shop, ShopItem item, ElementBox shopElement){
        this.shop = shop;
        this.item = item;

        // Null item means empty shop item element box
        if (item != null){
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
    }

    @Override
    public void initialize(ElementPanel panel) {
        super.initialize(panel);
        if (costText != null){
            panel.addElement(costText);
        }
    }

    public void select(){
        selected = true;
        System.out.println("selected "+this);
    }

    public void unselect(){
        selected = false;
        System.out.println("unselected "+this);
    }

    @Override
    public void beforeDraw() {
        canBuy = item != null && shop.getTeam().canBuy(item);

        Team team = shop.getTeam();
        setBorder(selected
                // if selected
                ? (canBuy
                        // if can buy
                        ? (shop.isFocused() ? team.getPointColor() : team.getFadedPointColor())

                        // if cannot buy
                        : (shop.isFocused() ? team.getDarkPointColor() : team.getDarkFadedPointColor()))

                // if not selected
                : (canBuy ? UI_FG_COLOR : UI_BORDER_COLOR)
        );


        if (costText != null){
            costText.setFg(canBuy ? UI_FG_COLOR : CANNOT_AFFORD_COLOR);
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        if (item != null){
            item.getItem().draw(g,
                    getX() + getWidth()/2.0,
                    getY() + 12,
                    getWidth()*0.8,
                    !canBuy);
        }
    }
}
