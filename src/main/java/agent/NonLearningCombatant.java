package agent;

import combat_data.Effect;
import combat_data.Move;
import combat_data.Weapon;

public class NonLearningCombatant extends LearningCombatant {

    public NonLearningCombatant() {
    }

    public NonLearningCombatant(String name, Weapon weapon) {
        super(name, weapon);
    }

    public NonLearningCombatant(String name, Weapon weapon, Double learningRate) {
        super(name, weapon, learningRate);
    }

    @Override
    public void learn(Move myMove, Effect myEffect, Move enemyMove, Effect enemyEffect, Integer distance) {
    }
}
