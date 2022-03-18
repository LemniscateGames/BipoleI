package scripts;

import lib.Battle;
import lib.Map;
import lib.Team;
import lib.display.BipoleIFrame;
import lib.units.EmptyLand;
import lib.units.Soldier;

public class RandomTiles {
    public static void main(String[] args) {
        BipoleIFrame frame = new BipoleIFrame();

        int rows = 10 + (int)(Math.random()*21);
        int cols = 10 + (int)(Math.random()*21);

        Map map = new Map(rows, cols);
        Team allies = Team.defaultAllies();
        Team enemies = Team.defaultEnemies();
        Battle battle = new Battle(map, allies, enemies);

        // place enemy claimedTiles
        for (int i=0; i<15; i++){
            map.placeTile(new EmptyLand(enemies), (int)(Math.random()*rows), (int)(Math.random()*rows));
        }
        // place enemy units
        for (int i=0; i<3; i++){
            map.placeTile(new Soldier(enemies), (int)(Math.random()*rows), (int)(Math.random()*rows));
        }

        // == Place claimedTiles randomly
        // place allied claimedTiles
        for (int i=0; i<15; i++){
            map.placeTile(new EmptyLand(allies), (int)(Math.random()*rows/2), (int)(Math.random()*cols/2));
        }
        // place allied units
        for (int i=0; i<3; i++){
            map.placeTile(new Soldier(allies), (int)(Math.random()*rows/2), (int)(Math.random()*cols/2));
        }

        frame.loadBattle(battle);
    }
}
