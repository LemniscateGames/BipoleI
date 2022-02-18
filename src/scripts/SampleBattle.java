package scripts;

import lib.Battle;
import lib.ClaimedTile;
import lib.Map;
import lib.Team;
import lib.display.BipoleIFrame;

public class SampleBattle {
    public static void main(String[] args) {
        BipoleIFrame frame = new BipoleIFrame();

        int rows = 10 + (int)(Math.random()*21);
        int cols = 10 + (int)(Math.random()*21);

        Map map = new Map(rows, cols);
        Team allies = Team.defaultAllies();
        Team enemies = Team.defaultEnemies();
        Battle battle = new Battle(map, allies, enemies);

        // Place claimedTiles randomly
        // place allied claimedTiles
        for (int i=0; i<15; i++){
            map.placeTile(new ClaimedTile(allies), (int)(Math.random()*rows), (int)(Math.random()*cols));
        }
        // place enemy claimedTiles
        for (int i=0; i<15; i++){
            map.placeTile(new ClaimedTile(enemies), (int)(Math.random()*8), (int)(Math.random()*8));
        }

        frame.loadBattle(battle);
    }
}
