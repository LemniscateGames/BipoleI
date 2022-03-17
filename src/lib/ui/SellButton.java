package lib.ui;

import lib.Team;
import lib.Tile;
import lib.Unit;

public class SellButton extends InfoButton {
    public SellButton(TileInfoElementBox infoBox) {
        super(infoBox);
        setText("Sell");
    }

    @Override
    public void beforeDraw() {
        super.beforeDraw();
        if (isEnabled()){
            Tile tile = getInfoBox().getDisplayedTile();
            int value = tile instanceof Unit ? ((Unit) tile).sellValue() : 0;
            setText(String.format("Sell (%d)", value));
        } else {
            setText("Sell");
        }
    }

    @Override
    public boolean isEnabled() {
        return getInfoBox().getDisplayedTile() instanceof Unit
                && ((Unit) getInfoBox().getDisplayedTile()).isSellable();
    }

    @Override
    public boolean isVisible(Team team) {
        return getInfoBox().getDisplayedTile() != null
                && getInfoBox().getDisplayedTile().isControllable(team);
    }
}