package BipoleI.lib;

public interface Buyable {
    int getCost();

    default boolean isBuyable(Team team){
        return team.getPoints() >= getCost();
    }
}
