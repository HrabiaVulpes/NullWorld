package agent;

import combat_data.Effect;
import combat_data.Weapon;

import static combat_data.ObjectsLists.RANDOMNESS_LEVEL;

public class GeneticallyLearningCombatant extends LearningCombatant {
    private long ID;

    public GeneticallyLearningCombatant() {
    }

    public GeneticallyLearningCombatant(String name, Weapon weapon, Long ID) {
        super(name, weapon);
        this.ID = ID;
    }

    @Override
    public void learn(Effect myEffect, Double enemyDamage, Integer distance) {
    }

    public GeneticallyLearningCombatant mutate(Long ID) {
        GeneticallyLearningCombatant result = new GeneticallyLearningCombatant(this.getName(), this.getWeapon(), ID);
        result.setCombatantMind(this.getCombatantMind().clone(RANDOMNESS_LEVEL));
        return result;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object obj) {
        return ((GeneticallyLearningCombatant) obj).ID == this.ID;
    }
}
