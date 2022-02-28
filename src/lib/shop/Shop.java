package lib.shop;

import lib.Team;
import lib.panels.BattlePanel;
import lib.panels.ElementPanel;
import lib.ui.ElementBox;
import lib.ui.ShopItemElementBox;
import lib.units.Castle;
import lib.units.Farmer;
import lib.units.Soldier;

import java.util.ArrayList;

public class Shop {
    // === constants
    public static final int SHOP_TOP_PADDING = 16;
    public static final int SHOP_LEFT_PADDING = 6;
    public static final int SHOP_RIGHT_PADDING = 6;
    public static final int SHOP_INNER_WIDTH = 128;
    public static final int SHOP_WIDTH = SHOP_INNER_WIDTH + SHOP_LEFT_PADDING + SHOP_RIGHT_PADDING;
    public static final int SHOP_ITEM_MARGIN = 6;

    public static final int COLS = 2;
    public static final int SHOP_ITEM_WIDTH = (SHOP_INNER_WIDTH /COLS) - SHOP_ITEM_MARGIN*2;
    public static final int SHOP_ITEM_TEXT_SPACE = 12;
    public static final int SHOP_ITEM_HEIGHT = SHOP_ITEM_WIDTH + SHOP_ITEM_TEXT_SPACE;
    public static final int ROWS = 6;

    // === fields
    private final Team team;
    private final ArrayList<ShopItem> items;
    private boolean focused;

    // elements
    private ElementBox shopBodyElement;
    private ShopItemElementBox[] itemElements;
    private int selectedItemIndex;

    public Shop(Team team){
        this.team = team;
        this.items = new ArrayList<>();
    }

    public void addItem(Buyable item, int cost){
        items.add(new ShopItem(item, cost));
    }
    public void addItem(Buyable item){
        items.add(new ShopItem(item, item.getCost()));
    }

    /** Should be called after all items are added. Initializes all elements to the passed panel. **/
    public void initializeElements(ElementPanel panel){
        // Container box for the shop
        shopBodyElement = new ElementBox(0, 0, SHOP_WIDTH, 50);
        panel.addElement(shopBodyElement);
        shopBodyElement.setxAlign(ElementBox.Alignment.END);
        shopBodyElement.setAlignPanelX(true);
        shopBodyElement.setFillPanelY(true);
        shopBodyElement.setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);

        // Add each item as a new elementbox
        initializeShopItemElements(panel);
    }

    /** Re-Initialize all shop item elements and remove current ones.
     * Should be called whenever the shop items arraylist is updated.
      */
    public void initializeShopItemElements(ElementPanel panel){
        // Clear all existing items if any and make new arrayList
        itemElements = new ShopItemElementBox[ROWS*COLS];
        for (int i=0; i<itemElements.length; i++){
            int row = i/COLS;
            int col = i%COLS;

            ShopItem item = i<items.size() ? items.get(i) : null;
            ShopItemElementBox itemElement = new ShopItemElementBox(this, item, shopBodyElement);
            itemElements[i] = itemElement;
            panel.addElement(itemElement);

            itemElement.setDimensions(SHOP_ITEM_WIDTH, SHOP_ITEM_HEIGHT);
            itemElement.setRelLeftElement(shopBodyElement);
            itemElement.setRelTopElement(shopBodyElement);
            itemElement.setxAlign(ElementBox.Alignment.START);
            itemElement.setyAlign(ElementBox.Alignment.START);

            itemElement.setLeft(SHOP_LEFT_PADDING + (SHOP_ITEM_MARGIN*2 + SHOP_ITEM_WIDTH)*col + SHOP_ITEM_MARGIN);
            itemElement.setTop(SHOP_TOP_PADDING + (SHOP_ITEM_MARGIN*2 + SHOP_ITEM_HEIGHT)*row + SHOP_ITEM_MARGIN);
        }
    }

    /** Ran when mode becomes SHOP_CURSOR. **/
    public void focusElement(){
        focused = true;
        shopBodyElement.setBg(ElementBox.UI_BG_COLOR);
        shopBodyElement.setBorder(ElementBox.UI_BORDER_COLOR_LIGHTER);
        for (ShopItemElementBox shopItem : itemElements){
            shopItem.setBg(ElementBox.UI_BG_COLOR);
        }
    }

    /** Ran when mode is about to not be SHOP_CURSOR. **/
    public void unfocusElement(){
        focused = false;
        shopBodyElement.setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);
        shopBodyElement.setBorder(ElementBox.UI_BORDER_COLOR_TRANSPARENT);
        for (ShopItemElementBox shopItem : itemElements){
            shopItem.setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);
        }
    }

    public void unselectItem(int index){
        itemElements[index].unselect();
    }

    public void selectItem(int index){
        selectedItemIndex = index;
        itemElements[selectedItemIndex].select();
    }

    // Accessors
    public Team getTeam() {
        return team;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public boolean isFocused() {
        return focused;
    }

    public ShopItemElementBox[] getItemElements() {
        return itemElements;
    }

    // Utility
    public static Shop generateDefaultShop(BattlePanel panel, Team team){
        Shop shop = new Shop(team);

        shop.addItem(new Soldier(team));
        shop.addItem(new Farmer(team));
        shop.addItem(new Castle(team));

        shop.initializeElements(panel);

        return shop;
    }
}
