package lib.ui;

import lib.Team;
import lib.Unit;
import lib.panels.ElementPanel;

import java.awt.*;

public abstract class InfoButton extends ElementBox {
    public static final Color DISABLED_TEXT_COLOR = new Color(160, 144, 144);
    public static final int HEIGHT = 20;
    public static final int PADDING = 4;
    public static final int OUTER_HEIGHT = (InfoButton.HEIGHT + InfoButton.PADDING*2);

    private final TileInfoElementBox infoBox;
    private boolean selected;

    public abstract boolean isEnabled();
    public abstract boolean isVisible(Team team);

    public InfoButton(TileInfoElementBox infoBox){
        super();
        this.infoBox = infoBox;

        setFont(ElementBox.GAME_FONT);
    }

    public void select(){
        selected = true;
    }

    public void unselect(){
        selected = false;
    }

    @Override
    public void initialize(ElementPanel panel) {
        super.initialize(panel);
    }

    @Override
    public void beforeDraw() {
        boolean enabled = isEnabled();

        if (infoBox.getDisplayedTile() instanceof Unit){
            Team team = ((Unit) infoBox.getDisplayedTile()).getTeam();
            setBorder(selected
                    // if selected
                    ? (enabled
                    // if enabled
                    ? (infoBox.isFocused() ? team.getPointColor() : team.getFadedPointColor())

                    // if cannot buy
                    : (infoBox.isFocused() ? team.getDarkPointColor() : team.getDarkFadedPointColor()))

                    // if not selected
                    : (enabled ? UI_FG_COLOR : UI_BORDER_COLOR)
            );
        }

//        setBorder(selected
//                // if selected
//                ? (enabled
//                // if enabled
//                ? (infoBox.isFocused() ? team.getPointColor() : UI_FG_COLOR)
//
//                // if cannot buy
//                : (infoBox.isFocused() ? team.getDarkPointColor() : UI_BORDER_COLOR))
//
//                // if not selected
//                : (enabled ? UI_FG_COLOR : UI_BORDER_COLOR)
//        );

        setFg(enabled ? UI_FG_COLOR : DISABLED_TEXT_COLOR);
    }

    public TileInfoElementBox getInfoBox() {
        return infoBox;
    }
}
