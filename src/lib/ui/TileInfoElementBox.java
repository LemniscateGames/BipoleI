package lib.ui;

import lib.Team;
import lib.Tile;
import lib.panels.ElementPanel;

import java.awt.*;
import java.util.ArrayList;

public class TileInfoElementBox extends ElementBox {
    /** The panel this element is on. **/
    private final ElementPanel panel;
    /** The elementbox that contains the title. **/
    private ElementBox title;
    /** The elementbox that contains the description. **/
    private ElementBox desc;

    /** The team this elementbox is being shown to, needed for visibility stuff. **/
    private final Team team;

    private Tile displayedTile;
    private ArrayList<InfoButton> buttons;
    private InfoButton sellButton;
    private InfoButton moveButton;

    /** Whether or not this element is focused. **/
    private boolean focused;

    private int selectedItemIndex;

    public static final int LEFT_PADDING = 4;
    public static final int TOP_PADDING = 4;

    public static final int INNER_WIDTH = 160;
    public static final int WIDTH = LEFT_PADDING + INNER_WIDTH;
    public static final int BUTTON_WIDTH = INNER_WIDTH - InfoButton.PADDING * 2 - LEFT_PADDING;

    public static final int TITLE_HEIGHT = 20;
    public static final int DESCRIPTION_HEIGHT = 100;
    public static final int BUTTON_SPACE_FOR = 3;
    public static final int BUTTON_HEIGHT = InfoButton.OUTER_HEIGHT * BUTTON_SPACE_FOR;
    public static final int HEIGHT = TOP_PADDING + TITLE_HEIGHT + DESCRIPTION_HEIGHT + BUTTON_HEIGHT;

    public TileInfoElementBox(ElementPanel panel, Team team) {
        super(0, 0, WIDTH, HEIGHT);
        this.panel = panel;
        this.team = team;
        buttons = new ArrayList<>();

//        setTransparentBg(true);
        setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);
    }

    @Override
    public void initialize(ElementPanel panel) {
        super.initialize(panel);

        // Title text here
        title = new ElementBox(LEFT_PADDING, 0, INNER_WIDTH-LEFT_PADDING-8, TITLE_HEIGHT);
        title.setTransparentBg(true);
        title.setxTextAlign(Alignment.START);
        title.setyTextAlign(Alignment.CENTER);
        title.setFont(ElementBox.GAME_FONT);
        title.setLeft(LEFT_PADDING + 8);
        title.setTop(TOP_PADDING + 8);
        panel.addElement(title);

        // Description
        desc = new ElementBox(LEFT_PADDING, 0, INNER_WIDTH-LEFT_PADDING-8, DESCRIPTION_HEIGHT);
        desc.setTransparentBg(true);
        desc.setxTextAlign(Alignment.START);
        desc.setyTextAlign(Alignment.START);
        desc.setFont(ElementBox.GAME_FONT_SMALL);
        desc.setWrapText(true);
        desc.setLeft(LEFT_PADDING + 8);
        desc.setTop(TOP_PADDING + 16 + title.getHeight());
        panel.addElement(desc);

        sellButton = new SellButton(this);
        addButton(sellButton);
        moveButton = new MoveButton(this);
        addButton(moveButton);
    }

    public void addButton(InfoButton button){
        buttons.add(button);
        button.setDimensions(BUTTON_WIDTH, InfoButton.HEIGHT);
        button.setLeft(LEFT_PADDING + InfoButton.PADDING);
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();

        int visibleButtons = 0;
        for (InfoButton button : buttons){
            if (button.isVisible(team)) {
                visibleButtons++;
                button.setTop(TITLE_HEIGHT + DESCRIPTION_HEIGHT + visibleButtons*InfoButton.OUTER_HEIGHT + InfoButton.PADDING);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        for (InfoButton button : buttons) {
            if (button.isVisible(team)){
                button.draw(g);
            }
        }
    }

    /** Ran when mode becomes SHOP_CURSOR. **/
    public void focusElement(){
        focused = true;
        setBg(ElementBox.UI_BG_COLOR);
        setBorder(ElementBox.UI_BORDER_COLOR_LIGHTER);
        for (InfoButton button : buttons){
            button.setBg(ElementBox.UI_BG_COLOR);
        }
    }

    /** Ran when mode is about to not be SHOP_CURSOR. **/
    public void unfocusElement(){
        focused = false;
        setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);
        setBorder(ElementBox.UI_BORDER_COLOR_TRANSPARENT);
        for (InfoButton button : buttons){
            button.setBg(ElementBox.UI_BG_COLOR_TRANSPARENT);
        }
    }

    public void unselectItem(int index){
        buttons.get(index).unselect();
    }

    public void selectItem(int index){
        selectedItemIndex = index;
        if (index == -1) return;
        buttons.get(index).select();
    }

    public InfoButton getButton(int i){
        return buttons.get(i);
    }

    public ArrayList<InfoButton> getButtons() {
        return buttons;
    }

    public Tile getDisplayedTile() {
        return displayedTile;
    }

    public void setDisplayedTile(Tile displayedTile) {
        this.displayedTile = displayedTile;
    }

    public ElementBox getTitle() {
        return title;
    }

    public ElementBox getDesc() {
        return desc;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isFocused() {
        return focused;
    }
}
