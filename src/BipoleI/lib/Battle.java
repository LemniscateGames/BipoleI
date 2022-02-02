package BipoleI.lib;

import BipoleI.BattlePanel;

import java.util.ArrayList;

public class Battle {
    private final Map map;
    private final ArrayList<Team> teams;

    /** (Optional) The BattlePanel this is displaying to. **/
    private BattlePanel panel;

    public Battle(Map map, ArrayList<Team> teams){
        this.map = map;
        this.teams = teams;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setPanel(BattlePanel panel) {
        this.panel = panel;
        map.setPanel(panel);
    }
}
