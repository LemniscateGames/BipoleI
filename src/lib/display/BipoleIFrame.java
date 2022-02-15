package lib.display;

import lib.Battle;

import javax.swing.*;
import java.awt.*;

public class BipoleIFrame extends JFrame {
    private JPanel panel;

    public BipoleIFrame() {
        super("Bipole I");
        setBounds(100, 100, 960, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loadBattle(Battle battle){
        panel = new BattlePanel(battle);

        setContentPane(panel);
        setVisible(true);
    }
}
