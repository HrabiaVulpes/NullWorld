package agent;

import ai.NeuralNetwork;
import combat_data.*;

import java.util.*;

public class Combatant {
    private final Long outputStart;
    public NeuralNetwork combatantMind;
    public String name;
    public Integer hitPoints;
    public Weapon weapon;
    public List<States> statesList;
    public Move wantedMove = null;
    public Move move = null;

    public Combatant(String name, Weapon weapon) {
        this.name = name;
        this.hitPoints = 30;
        this.weapon = weapon;
        this.statesList = new ArrayList<>();
        this.combatantMind = new NeuralNetwork(17, 16, 16, 16, 15); // 65L <- start output
        this.outputStart = 3 * 16 + 17L;
    }

    public void setStates(List<States> enemyStates, Integer enemyDistance) {
        Map<Long, Double> input = new HashMap<>();
        input.put(0L, statesList.contains(States.ABOVE) ? 1.0 : 0.0);
        input.put(1L, statesList.contains(States.TURNED) ? 1.0 : 0.0);
        input.put(2L, statesList.contains(States.CROUCHED) ? 1.0 : 0.0);
        input.put(3L, statesList.contains(States.KNOCKED) ? 1.0 : 0.0);
        input.put(4L, statesList.contains(States.WEAPON_SIDE) ? 1.0 : 0.0);
        input.put(5L, statesList.contains(States.WEAPON_EXTENDED) ? 1.0 : 0.0);
        input.put(6L, statesList.contains(States.WEAPON_LOW) ? 1.0 : 0.0);
        input.put(7L, statesList.contains(States.WEAPON_HIGH) ? 1.0 : 0.0);
        input.put(8L, enemyDistance * 1.0);
        input.put(9L, enemyStates.contains(States.ABOVE) ? 1.0 : 0.0);
        input.put(10L, enemyStates.contains(States.TURNED) ? 1.0 : 0.0);
        input.put(11L, enemyStates.contains(States.CROUCHED) ? 1.0 : 0.0);
        input.put(12L, enemyStates.contains(States.KNOCKED) ? 1.0 : 0.0);
        input.put(13L, enemyStates.contains(States.WEAPON_SIDE) ? 1.0 : 0.0);
        input.put(14L, enemyStates.contains(States.WEAPON_EXTENDED) ? 1.0 : 0.0);
        input.put(15L, enemyStates.contains(States.WEAPON_LOW) ? 1.0 : 0.0);
        input.put(16L, enemyStates.contains(States.WEAPON_HIGH) ? 1.0 : 0.0);

        combatantMind.setValues(input);
    }

    private MoveTypes getIdea() {
        combatantMind.calculateFullPass();
        Map<Double, MoveTypes> ideas = new HashMap<>();

        MoveTypes.getAll()
                .forEach(
                        type -> ideas.put(
                                combatantMind.getNodeById(outputStart + type.getId()).value,
                                type
                        )
                );

        Double best = Collections.max(ideas.keySet());
        return ideas.get(best);
    }

    public void pickMove() {
        MoveTypes choosen = getIdea();
        move = weapon.getOptionByType(choosen);
        wantedMove = weapon.getOptionByType(choosen);

        ArrayList<States> currentStates = new ArrayList<>(this.statesList);
        currentStates.retainAll(move.getUnavailableOn());

        if (!currentStates.isEmpty()) move = weapon.getOptionByType(MoveTypes.CLOSE_IN);
    }

    public void learn(Effect myEffect, Effect enemyEffect, Integer distance) {
        if (wantedMove.getType() != move.getType()){
            combatantMind.expectValues(Map.of(wantedMove.getType().getId() + outputStart, 0.0));
        }

        combatantMind.expectValues(Map.of(move.getType().getId() + outputStart, gradeMove(myEffect, enemyEffect, distance)));
        combatantMind.recalculateFullPass();
        combatantMind.updateWeights();
    }

    private Double gradeMove(Effect myEffect, Effect enemyEffect, Integer distance) {
        if (distance > weapon.getLength() && move.getType() == MoveTypes.CLOSE_IN) return 1.0;
        if (distance > weapon.getLength() && move.getType() != MoveTypes.CLOSE_IN) return 0.0;

        if (myEffect == Effect.HIT && enemyEffect == Effect.HIT) return 1.0;
        if (myEffect == Effect.HIT && enemyEffect == Effect.CRIT) return 0.0;
        if (myEffect == Effect.HIT && enemyEffect == Effect.PARRY) return 1.0;
        if (myEffect == Effect.HIT && enemyEffect == Effect.MISS) return 1.0;

        if (myEffect == Effect.CRIT && enemyEffect == Effect.HIT) return 1.0;
        if (myEffect == Effect.CRIT && enemyEffect == Effect.CRIT) return 1.0;
        if (myEffect == Effect.CRIT && enemyEffect == Effect.PARRY) return 1.0;
        if (myEffect == Effect.CRIT && enemyEffect == Effect.MISS) return 1.0;

        if (myEffect == Effect.PARRY && enemyEffect == Effect.HIT) return 0.0;
        if (myEffect == Effect.PARRY && enemyEffect == Effect.CRIT) return 0.0;
        if (myEffect == Effect.PARRY && enemyEffect == Effect.PARRY) return 1.0;
        if (myEffect == Effect.PARRY && enemyEffect == Effect.MISS) return 1.0;

        if (myEffect == Effect.MISS && enemyEffect == Effect.HIT) return 0.0;
        if (myEffect == Effect.MISS && enemyEffect == Effect.CRIT) return 0.0;
        if (myEffect == Effect.MISS && enemyEffect == Effect.PARRY) return 0.0;
        if (myEffect == Effect.MISS && enemyEffect == Effect.MISS) return 0.0;

        return 0.0;
    }
}
