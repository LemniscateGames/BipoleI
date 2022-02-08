package BipoleI.lib.shop;

import BipoleI.lib.Team;

public interface Buyable {
    int getCost();

    default boolean isBuyable(Team team){
        return team.getPoints() >= getCost();
    }
}
