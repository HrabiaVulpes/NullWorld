package agent;

import ai.Mind;
import ai.MindFuck;
import combat_data.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LearningCombatant extends Player {
    public Mind combatantMind;
    public Move wantedMove;

    public LearningCombatant() {
    }

    /**
     * @param name   - name of combatant
     * @param weapon - weapon he uses
     */
    public LearningCombatant(String name, Weapon weapon) {
        super(name, weapon);
        whoControl = "AI";

        this.combatantMind = new Mind();
        generateKnowledgeArray();
        generateDecisionsArray();
        this.combatantMind.buildBrain(0.3);
    }

    public LearningCombatant(String name, Weapon weapon, Integer amountOfHiddenLayers) {
        super(name, weapon);
        whoControl = "AI";

        this.combatantMind = new Mind();
        generateKnowledgeArray();
        generateDecisionsArray();
        this.combatantMind.buildBrain(amountOfHiddenLayers);
    }

    /**
     * @param name         - name of combatant
     * @param weapon       - weapon he uses
     * @param learningRate - how strong he updates weights
     */
    public LearningCombatant(String name, Weapon weapon, Double learningRate) {
        super(name, weapon);
        whoControl = "AI";

        this.combatantMind = new Mind();
        generateKnowledgeArray();
        generateDecisionsArray();
        this.combatantMind.buildBrain(learningRate);
    }

    /**
     * @param enemyStates   - states of the enemy
     * @param enemyDistance - distance to enemy
     * @param enemyWeapon   - weapon of the enemy
     */
    @Override
    public void setStates(Collection<States> enemyStates, Integer enemyDistance, Weapon enemyWeapon) {
        Arrays.asList(States.values()).forEach(
                state -> {
                    try {
                        combatantMind.letItKnow("MY_" + state, statesList.contains(state) ? 1.0 : 0.0);
                        combatantMind.letItKnow("ENEMY_" + state, enemyStates.contains(state) ? 1.0 : 0.0);
                    } catch (MindFuck mindFuck) {
                        mindFuck.printStackTrace();
                    }
                }
        );

        DamageTypes.effectiveTypes().forEach(
                damage -> {
                    try {
                        combatantMind.letItKnow("MY_" + damage, weapon.getEfficiencies().get(damage));
                        combatantMind.letItKnow("ENEMY_" + damage, enemyWeapon.getEfficiencies().get(damage));
                    } catch (MindFuck mindFuck) {
                        mindFuck.printStackTrace();
                    }
                }
        );

        try {
            combatantMind.letItKnow("MY_WEAPON_LENGTH", weapon.getLength() * 1.0);
            combatantMind.letItKnow("ENEMY_WEAPON_LENGTH", enemyWeapon.getLength() * 1.0);
            combatantMind.letItKnow("DISTANCE", enemyDistance * 1.0);
        } catch (MindFuck mindFuck) {
            mindFuck.printStackTrace();
        }

    }

    private void generateKnowledgeArray() {
        Arrays.asList(States.values()).forEach(
                state -> {
                    combatantMind.registerKnowledge("MY_" + state);
                    combatantMind.registerKnowledge("ENEMY_" + state);
                }
        );

        Arrays.asList(DamageTypes.values()).forEach(
                damage -> {
                    combatantMind.registerKnowledge("MY_" + damage);
                    combatantMind.registerKnowledge("ENEMY_" + damage);
                }
        );

        combatantMind.registerKnowledge("MY_WEAPON_LENGTH");
        combatantMind.registerKnowledge("ENEMY_WEAPON_LENGTH");
        combatantMind.registerKnowledge("DISTANCE");
    }

    private void generateDecisionsArray() {
        weapon.getOptions().forEach(combatOption -> combatantMind.registerDecision(combatOption.getType().toString()));
    }

    private MoveTypes getIdeaWithWeights() {
        return MoveTypes.valueOf(combatantMind.nowThink().getWeightedDecision("WAIT"));
    }

    @Override
    public void pickMove() {
        MoveTypes chosen = getIdeaWithWeights();
        move = weapon.getOptionByType(chosen);
        wantedMove = weapon.getOptionByType(chosen);

        List<States> currentStates = new ArrayList<>(this.statesList);
        currentStates.retainAll(move.getUnavailableOn());

        if (!currentStates.isEmpty())
            move = weapon.getOptionByType(MoveTypes.WAIT);
    }

    @Override
    public void learn(Move myMove, Effect myEffect, Move enemyMove, Effect enemyEffect, Integer distance) {
        try {
            if (!wantedMove.getType().name().equals(move.getType().name()))
                combatantMind.resultIs(wantedMove.getType().toString(), -1.0);

            combatantMind.resultIs(
                    move.getType().toString(),
                    gradeMove(myMove, myEffect, enemyMove, enemyEffect, distance)
            );
        } catch (MindFuck mindFuck) {
            mindFuck.printStackTrace();
        }
        combatantMind.rethinkIt();
    }

    private Double gradeMove(Move myMove, Effect myEffect, Move enemyMove, Effect enemyEffect, Integer distance) {
        if (myMove.getDamageType() != DamageTypes.NONE) {
            if (myEffect == Effect.CRIT && enemyEffect == Effect.MISS) return 1.0;
            if (myEffect == Effect.HIT && enemyEffect == Effect.MISS) return 0.9;

            if (myEffect == Effect.CRIT && enemyEffect == Effect.HIT) return 0.6;
            if (myEffect == Effect.HIT && enemyEffect == Effect.HIT) return 0.5;

            if (myEffect == Effect.CRIT && enemyEffect == Effect.CRIT) return 0.5;
            if (myEffect == Effect.HIT && enemyEffect == Effect.CRIT) return 0.4;

            if (myEffect == Effect.PARRY && enemyMove.getType() != MoveTypes.BLOCK)
                return 0.5;                         // Aga thinks this is wrong
            if (myEffect == Effect.PARRY && enemyMove.getType() == MoveTypes.BLOCK) return 0.1;

            if (myEffect == Effect.MISS && enemyEffect == Effect.MISS) return 0.4;
            if (myEffect == Effect.MISS) return 0.1;
        } else {
            if (myMove.getType() == MoveTypes.CLOSE_IN) {
                if (distance > weapon.getLength() && enemyEffect == Effect.MISS) return 1.0;
                if (distance > weapon.getLength() && enemyEffect != Effect.MISS) return 0.2;
                if (distance <= weapon.getLength() && enemyEffect == Effect.MISS) return 0.5;
                if (distance <= weapon.getLength() && enemyEffect != Effect.MISS) return 0.1;
            }
            if (myMove.getType() == MoveTypes.BLOCK) {
                if (enemyEffect == Effect.PARRY) return 0.9;
                else return 0.3;
            }
            if (enemyEffect == Effect.CRIT) return 0.1;
            if (enemyEffect == Effect.HIT) return 0.2;
            if (enemyEffect == Effect.MISS && enemyMove.getDamageType() != DamageTypes.NONE) return 0.9;
            if (enemyEffect == Effect.MISS && enemyMove.getDamageType() == DamageTypes.NONE)
                return 0.3;                // Aga thinks may need increasing
        }

        return 0.5;
    }

    public Mind getCombatantMind() {
        return combatantMind;
    }

    public void setCombatantMind(Mind combatantMind) {
        this.combatantMind = combatantMind;
    }

    public Move getWantedMove() {
        return wantedMove;
    }

    public void setWantedMove(Move wantedMove) {
        this.wantedMove = wantedMove;
    }

    @Override
    public boolean equals(Object obj) {
        return ((LearningCombatant) obj).name.equals(this.name);
    }
}
