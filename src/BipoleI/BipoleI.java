package BipoleI;

import BipoleI.lib.*;
import BipoleI.lib.battlepanel.BattlePanel;
import BipoleI.lib.units.Castle;
import BipoleI.lib.units.ClaimedTile;
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
            map.placeUnit(i, i, new ClaimedTile(map, allies));
        }

        for (int i=0; i<5; i++){
            map.placeUnit(7-i, i, new ClaimedTile(map, enemies));
        }

        map.placeUnit(7,7, new Castle(map, allies));
        map.placeUnit(7, 6, new Soldier(map, allies));
        map.placeUnit(7, 5, new Farmer(map, allies));

        map.placeUnit(0,7, new Castle(map, enemies));
        map.placeUnit(0, 6, new Soldier(map, enemies));
        map.placeUnit(0, 5, new Farmer(map, enemies));

        ArrayList<Team> teams = new ArrayList<>();
        teams.add(allies);
        teams.add(enemies);
        Battle battle = new Battle(map, teams);

        loadBattle(frame, battle);
    }

    public static void loadBattle(JFrame frame, Battle battle){
        BattlePanel panel = new BattlePanel();
        panel.setZoom(80.0f);

        frame.setContentPane(panel);
        frame.setVisible(true);

        panel.setBackground(new Color(16,16,16));
        panel.setBattle(battle);
    }
}
