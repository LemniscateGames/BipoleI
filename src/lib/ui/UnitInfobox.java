package lib.ui;

import lib.Unit;
import lib.panels.ElementPanel;

import java.awt.*;

public class UnitInfobox extends ElementBox {
    /** The unit currently being displayed. **/
    private Unit unit;
    /** The panel this element is on. **/
    private final ElementPanel panel;
    /** The elementbox that contains the unit title. **/
    private ElementBox unitTitle;

    public UnitInfobox(ElementPanel panel) {
        super(0, 0, 160, 160);
        this.panel = panel;

        setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);
    }

    @Override
    public void initialize(ElementPanel panel) {
        super.initialize(panel);

        // Title text here
        unitTitle = new ElementBox(0, 0, 160, 40);
        unitTitle.setTransparentBg(true);
        unitTitle.setxAlign(Alignment.START);
        unitTitle.setyAlign(Alignment.START);
        unitTitle.setxTextAlign(Alignment.START);
        unitTitle.setFont(ElementBox.GAME_FONT);
        unitTitle.setLeft(8);
        unitTitle.setTop(8);
        panel.addElement(unitTitle);
    }

    public void displayUnit(Unit unit){
        this.unit = unit;
        unitTitle.setFg(unit.getTeam().getColor(0.25, 0));
        unitTitle.setText(unit.getClass().getSimpleName());
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
    }
}
