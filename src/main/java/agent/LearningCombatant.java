package agent;

import ai.Mind;
import ai.MindFuck;
import combat_data.*;

import java.util.*;

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

        if (!currentStates.isEmpty()) move = weapon.getOptionByType(MoveTypes.WAIT);
    }

    @Override
    public void learn(Effect myEffect, Double enemyDamage, Integer distance) {
        try {
            if (!wantedMove.getType().name().equals(move.getType().name()))
                combatantMind.resultIs(wantedMove.getType().toString(), 0.0);

            combatantMind.resultIs(move.getType().toString(), gradeMove(myEffect, enemyDamage, distance));
        } catch (MindFuck mindFuck) {
            mindFuck.printStackTrace();
        }
        combatantMind.rethinkIt();
    }

    private Double gradeMove(Effect myEffect, Double enemyDamage, Integer distance) {
        if (move.getType() == MoveTypes.CLOSE_IN) {
            if (distance > weapon.getLength())
                return 1.0;
            if (distance < weapon.getLength())
                return 0.0;
        }

        if (myEffect == Effect.HIT || myEffect == Effect.CRIT) {
            if (damageDealt(myEffect) >= enemyDamage) return 1.0;
            else return 0.5;
        }

        if (myEffect == Effect.PARRY) {
            if (enemyDamage > 0.0) return 0.0;
            else return 1.0;
        }

        if (myEffect == Effect.MISS) return 0.0;

        return 0.0;
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
