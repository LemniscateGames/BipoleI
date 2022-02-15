package scripts;

import lib.Battle;
import lib.Map;
import lib.display.BipoleIFrame;

public class SampleBattle {
    public static void main(String[] args) {
        BipoleIFrame frame = new BipoleIFrame();

        Map map = new Map(8, 8);

        Battle battle = new Battle(map);

        frame.loadBattle(battle);
    }
}
