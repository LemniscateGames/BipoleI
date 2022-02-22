package scripts;

import lib.Battle;
import lib.ClaimedTile;
import lib.Map;
import lib.Team;
import lib.display.BipoleIFrame;
import lib.units.Castle;
import lib.units.EmptyLand;
import lib.units.Soldier;

public class SampleBattle {
    public static void main(String[] args) {
        BipoleIFrame frame = new BipoleIFrame();

        Map map = new Map(8, 8);
        Team allies = Team.defaultAllies();
        Team enemies = Team.defaultEnemies();
        Battle battle = new Battle(map, allies, enemies);

        map.placeTile(new Castle(allies), 7, 0);
        map.placeTile(new EmptyLand(allies), 6, 0);
        map.placeTile(new EmptyLand(allies), 7, 1);
        map.placeTile(new Soldier(allies), 6, 1);

        map.placeTile(new Castle(enemies), 0, 7);
        map.placeTile(new EmptyLand(enemies), 0, 6);
        map.placeTile(new EmptyLand(enemies), 1, 7);
        map.placeTile(new Soldier(enemies), 1, 6);

        frame.loadBattle(battle);
    }
}
