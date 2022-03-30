package lib.ui;

import lib.Team;
import lib.Unit;

public class MoveButton extends InfoButton {
    public MoveButton(TileInfoElementBox infoBox) {
        super(infoBox);
        setText("Move");
    }

    @Override
    public boolean isEnabled() {
        return getInfoBox().getDisplayedTile() instanceof Unit
                && ((Unit) getInfoBox().getDisplayedTile()).canMove();
    }

    @Override
    public boolean isVisible(Team team) {
        return getInfoBox().getDisplayedTile() != null
                && getInfoBox().getDisplayedTile().isControllable(team);
    }
}