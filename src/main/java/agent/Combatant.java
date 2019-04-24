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
    public Integer victoriesCount = 0;
    public Integer lossesCount = 0;

    public Combatant(String name, Weapon weapon) {
        this.name = name;
        this.hitPoints = 100;
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
        if (wantedMove.getType() != move.getType()) {
            combatantMind.expectValues(Map.of(wantedMove.getType().getId() + outputStart, 0.0));
        }

        combatantMind.expectValues(Map.of(move.getType().getId() + outputStart, gradeMove(myEffect, enemyEffect, distance)));
        combatantMind.recalculateFullPass();
        combatantMind.updateWeights();
    }

    private Double gradeMove(Effect myEffect, Effect enemyEffect, Integer distance) {
        if (distance > weapon.getLength() && move.getType() == MoveTypes.CLOSE_IN) return 1.0;
        if (distance > weapon.getLength() && move.getType() != MoveTypes.CLOSE_IN) return 0.0;

        if (myEffect == Effect.CRIT && enemyEffect == Effect.HIT) return 1.0;
        if (myEffect == Effect.CRIT && enemyEffect == Effect.CRIT) return 1.0;
        if (myEffect == Effect.CRIT && enemyEffect == Effect.PARRY) return 1.0;
        if (myEffect == Effect.CRIT && enemyEffect == Effect.MISS) return 1.0;

        if (myEffect == Effect.HIT && enemyEffect == Effect.CRIT) return 0.0;
        if (myEffect == Effect.HIT && enemyEffect == Effect.HIT) return 1.0;
        if (myEffect == Effect.HIT && enemyEffect == Effect.PARRY) return 1.0;
        if (myEffect == Effect.HIT && enemyEffect == Effect.MISS) return 1.0;

        if (myEffect == Effect.PARRY && enemyEffect == Effect.HIT) return 0.0;
        if (myEffect == Effect.PARRY && enemyEffect == Effect.CRIT) return 0.0;
        if (myEffect == Effect.PARRY && enemyEffect == Effect.PARRY) return 1.0;
        if (myEffect == Effect.PARRY && enemyEffect == Effect.MISS) return 1.0;

        if (myEffect == Effect.MISS && enemyEffect == Effect.HIT) return 0.0;
        if (myEffect == Effect.MISS && enemyEffect == Effect.CRIT) return 0.0;
        if (myEffect == Effect.MISS && enemyEffect == Effect.PARRY) return 0.0;
        if (myEffect == Effect.MISS && enemyEffect == Effect.MISS) return 1.0;

        return 0.0;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Combatant) obj).name.equals(this.name);
    }

    public Combatant healUp() {
        this.hitPoints = 100;
        return this;
    }

    public Double damageDealt() {
        return 10.0 * this.weapon.getEfficiencies().get(this.move.getDamageType());
    }

    public Long getOutputStart() {
        return outputStart;
    }

    public NeuralNetwork getCombatantMind() {
        return combatantMind;
    }

    public void setCombatantMind(NeuralNetwork combatantMind) {
        this.combatantMind = combatantMind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public List<States> getStatesList() {
        return statesList;
    }

    public void setStatesList(List<States> statesList) {
        this.statesList = statesList;
    }

    public Move getWantedMove() {
        return wantedMove;
    }

    public void setWantedMove(Move wantedMove) {
        this.wantedMove = wantedMove;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Integer getVictoriesCount() {
        return victoriesCount;
    }

    public void setVictoriesCount(Integer victoriesCount) {
        this.victoriesCount = victoriesCount;
    }

    public Integer getLossesCount() {
        return lossesCount;
    }

    public void setLossesCount(Integer lossesCount) {
        this.lossesCount = lossesCount;
    }
}
