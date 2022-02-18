package scripts;

import lib.Battle;
import lib.ClaimedTile;
import lib.Map;
import lib.Team;
import lib.display.BipoleIFrame;
import lib.units.Soldier;

public class SampleBattle {
    public static void main(String[] args) {
        BipoleIFrame frame = new BipoleIFrame();

        Map map = new Map(8, 8);
        Team allies = Team.defaultAllies();
        Team enemies = Team.defaultEnemies();
        Battle battle = new Battle(map, allies, enemies);

        map.placeTile(new Soldier(allies), 7, 0);
        map.placeTile(new ClaimedTile(allies), 6, 0);
        map.placeTile(new ClaimedTile(allies), 7, 1);

        map.placeTile(new Soldier(enemies), 0, 7);
        map.placeTile(new ClaimedTile(enemies), 0, 6);
        map.placeTile(new ClaimedTile(enemies), 1, 7);

        frame.loadBattle(battle);
    }
}
