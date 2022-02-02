package BipoleI.lib;

import java.util.ArrayList;

public class Battle {
    private final Map map;
    private final ArrayList<Team> teams;

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
}
