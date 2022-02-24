package lib.shop;

import lib.Team;
import lib.panels.BattlePanel;
import lib.ui.ElementBox;
import lib.ui.ShopItemElementBox;
import lib.units.Castle;
import lib.units.Soldier;

import java.util.ArrayList;

public class Shop {
    // === constants
    public static final int SHOP_TOP_PADDING = 16;
    public static final int SHOP_SIDE_PADDING = 4;
    public static final int SHOP_INNER_WIDTH = 128;
    public static final int SHOP_WIDTH = SHOP_INNER_WIDTH + SHOP_SIDE_PADDING*2;
    public static final int SHOP_ITEM_MARGIN = 4;

    public static final int COLS = 2;
    public static final int SHOP_ITEM_WIDTH = (SHOP_INNER_WIDTH /COLS) - SHOP_ITEM_MARGIN*2;
    public static final int SHOP_ITEM_TEXT_SPACE = 12;
    public static final int SHOP_ITEM_HEIGHT = SHOP_ITEM_WIDTH + SHOP_ITEM_TEXT_SPACE;

    // === fields
    private final ArrayList<ShopItem> items;

    // elements
    private ElementBox shopBodyElement;
    private ArrayList<ElementBox> itemElements;

    public Shop(){
        this.items = new ArrayList<>();
    }

    public void addItem(Buyable item, int cost){
        items.add(new ShopItem(item, cost));
    }
    public void addItem(Buyable item){
        items.add(new ShopItem(item, item.getCost()));
    }

    /** Should be called after all items are added. Initializes all elements to the passed panel. **/
    public void initializeElements(BattlePanel panel){
        // Container box for the shop
        shopBodyElement = new ElementBox(0, 0, SHOP_WIDTH, 50);
        panel.addElement(shopBodyElement);
        shopBodyElement.setxAlign(ElementBox.Alignment.END);
        shopBodyElement.setAlignPanelX(true);
        shopBodyElement.setFillPanelY(true);

        // Add each item as a new elementbox
        initializeShopItemElements(panel);
    }

    /** Re-Initialize all shop item elements and remove current ones.
     * Should be called whenever the shop items arraylist is updated.
      */
    public void initializeShopItemElements(BattlePanel panel){
        // Clear all existing items if any and make new arrayList
        itemElements = new ArrayList<>();
        for (int i=0; i<items.size(); i++){
            int row = i/COLS;
            int col = i%COLS;

            ShopItem item = items.get(i);
            ElementBox itemElement = new ShopItemElementBox(this, item, shopBodyElement);
            itemElements.add(itemElement);
            panel.addElement(itemElement);

            itemElement.setDimensions(SHOP_ITEM_WIDTH, SHOP_ITEM_HEIGHT);
            itemElement.setRelLeftElement(shopBodyElement);
            itemElement.setRelTopElement(shopBodyElement);
            itemElement.setxAlign(ElementBox.Alignment.START);
            itemElement.setyAlign(ElementBox.Alignment.START);

            itemElement.setLeft(SHOP_SIDE_PADDING + (SHOP_ITEM_MARGIN*2 + SHOP_ITEM_WIDTH)*col + SHOP_ITEM_MARGIN);
            itemElement.setTop(SHOP_TOP_PADDING + (SHOP_ITEM_MARGIN*2 + SHOP_ITEM_HEIGHT)*row + SHOP_ITEM_MARGIN);
        }
    }

    // Utility
    public static Shop generateDefaultShop(BattlePanel panel, Team team){
        Shop shop = new Shop();

        shop.addItem(new Soldier(team));
        shop.addItem(new Castle(team));

        shop.initializeElements(panel);

        return shop;
    }
}
