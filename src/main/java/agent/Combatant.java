package agent;

import combat_data.Move;
import combat_data.States;
import combat_data.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Combatant {
    public String name;
    public Integer hitPoints;
    public Weapon weapon;
    public List<States> statesList;
    public Move move = null;

    public Combatant(String name, Weapon weapon) {
        this.name = name;
        this.hitPoints = 30;
        this.weapon = weapon;
        this.statesList = new ArrayList<>();
    }
}
