package BipoleI.lib.battlepanel;

import BipoleI.lib.*;
import BipoleI.lib.ui.AlignedBox;
import BipoleI.lib.ui.ElementBox;
import BipoleI.lib.shop.Buyable;
import BipoleI.lib.shop.Shop;
import BipoleI.lib.shop.ShopItemBox;
import BipoleI.lib.ui.RootBox;

import java.awt.*;

/** Class that generates the ElementBox (instantiated as FixedBox) to store all ElementBox Ui elements. **/
public class BattlePanelElementManager {
    private final BattlePanel panel;

    private final RootBox root;

    private final ElementBox points;

    private final ElementBox shop;
    private Number shopWidth = 80;
    private Color shopBgColor = BattlePanel.UI_BG_COLOR_TRANSPARENT;
    private Color shopBorderColor = BattlePanel.UI_BORDER_COLOR_TRANSPARENT;

    // Run at initialization
    public BattlePanelElementManager(BattlePanel panel){
        this.panel = panel;

        root = new RootBox();

        points = new AlignedBox(root, Alignment.TOP);
        points.setDimensions(200, 50);
        points.setFont(BattlePanel.GAME_FONT_BIG);
        points.setTextAlignment(Alignment.CENTER);

        shop = new AlignedBox(root, Alignment.RIGHT);
        shop.setAlignment(Alignment.TOP);
        shop.setDimensions(shopWidth, 0);
        shop.setPad(10);
        shop.setBgColor(shopBgColor);
        shop.setBorderColor(shopBorderColor);

        root.printEverything();
        points.printEverything();
        shop.printEverything();
    }

    /** This method is run whenever the Battle is changed on the BattlePanel. **/
    public void initializeWithBattle(Battle battle){
        points.setFgColor(battle.allies().getPointColor());
        Shop battleShop = battle.allies().getShop();

        Number shopItemWidth = shop.outerWidth(shopWidth);
        for (Buyable item : battleShop.getItems()){
            ElementBox shopItem = new ShopItemBox(shop, item);
            shopItem.setWidth(shopItemWidth);
            shopItem.setHeight(72);
            shopItem.printEverything();
        }
    }

    // Run on every frame
    public void draw(Graphics g){
        points.setText(panel.getBattle().allies().getPoints() + " pts");

        shop.fillHeight(panel.getHeight());

        root.drawAsRoot(g, panel.getWidth(), panel.getHeight());
    }

    public RootBox getRoot() {
        return root;
    }


}
