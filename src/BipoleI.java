import lib.Battle;
import lib.display.BipoleIFrame;

import javax.swing.*;

public class BipoleI {
    public static void main(String[] args) {
        BipoleIFrame frame = new BipoleIFrame();
        frame.loadBattle(new Battle(null));
    }
}
