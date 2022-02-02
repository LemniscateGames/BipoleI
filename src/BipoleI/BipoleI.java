package BipoleI;

import BipoleI.lib.Battle;
import BipoleI.lib.EmptyTile;
import BipoleI.lib.Map;
import BipoleI.lib.Team;
import BipoleI.lib.units.Castle;
import BipoleI.lib.units.Farmer;
import BipoleI.lib.units.Soldier;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BipoleI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bipole I");
        frame.setBounds(100, 100, 960, 540);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Map map = new Map(8,8);
        Team allies = new Team(0);
        Team enemies = new Team(1);

        for (int i=0; i<5; i++){
            map.placeUnit(i, i, new EmptyTile(allies));
        }

        for (int i=0; i<5; i++){
            map.placeUnit(7-i, i, new EmptyTile(enemies));
        }

        map.placeUnit(7,7, new Castle(allies));
        map.placeUnit(6, 6, new Soldier(allies));
        map.placeUnit(7, 5, new Farmer(allies));

        map.placeUnit(0,7, new Castle(enemies));
        map.placeUnit(1, 6, new Soldier(enemies));
        map.placeUnit(0, 5, new Farmer(enemies));

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(allies);
        teams.add(enemies);
        Battle battle = new Battle(map, teams);

        loadBattle(frame, battle);
    }

    public static void loadBattle(JFrame frame, Battle battle){
        BattlePanel panel = new BattlePanel();

        frame.setContentPane(panel);
        frame.setVisible(true);

        panel.setBackground(new Color(10,10,10));
        panel.setBattle(battle);
    }
}
