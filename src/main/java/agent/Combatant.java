package agent;

import combat_data.States;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Combatant {
    public String name;
    public Integer hitPoints;
    public List<States> statesList;

    public Combatant(String name) {
        this.name = name;
        this.hitPoints = 30;
        this.statesList = new ArrayList<>(Collections.singleton(States.FAR));
    }
}
