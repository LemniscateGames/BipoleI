package lib.display;

import lib.Battle;

import javax.swing.*;
import java.awt.*;

public class BattlePanel extends JPanel {
    private Battle battle;

    public BattlePanel(Battle battle) {
        this.battle = battle;
        setBackground(new Color(16,16,16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw empty tile borders if there are no adjacent units to current tile
        //   Draw top and left sides of all tiles
        for (int r=0; r<battle.getMap().numRows(); r++){
            for (int c=0; c<battle.getMap().numCols(); c++){

            }
        }

    }
}
