package lib;

public class Battle {
    private Team[] teams;
    private Map map;

    public Battle(Map map, Team... teams){
        this.map = map;
        this.teams = teams;
    }

    public Battle(Map map){
        this.map = map;
        this.teams = new Team[]{Team.defaultAllies(), Team.defaultEnemies()};
    }

    // accessors
    public Team getTeam(int i){
        return teams[i];
    }

    public Team[] getTeams() {
        return teams;
    }

    public Map getMap() {
        return map;
    }
}
