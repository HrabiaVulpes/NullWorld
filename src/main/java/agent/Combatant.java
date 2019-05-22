package agent;

import ai.NeuralNetwork;
import combat_data.*;

import java.util.*;
import java.util.stream.Collectors;

public class Combatant {
    private final Long outputStart = 27L + 30;
    public NeuralNetwork combatantMind;
    public String name;
    public Integer hitPoints;
    public Weapon weapon;
    public List<States> statesList;
    public Move wantedMove = null;
    public Move move = null;
    public Integer victoriesCount = 0;
    public Integer lossesCount = 0;

    public Combatant() {
    }

    /**
     *
     * @param name - name of combatant
     * @param weapon - weapon he uses
     */
    public Combatant(String name, Weapon weapon) {
        this.name = name;
        this.hitPoints = 100;
        this.weapon = weapon;
        this.statesList = new ArrayList<>();
        this.combatantMind = new NeuralNetwork(27, 30, 16);
    }

    /**
     *
     * @param name - name of combatant
     * @param weapon - weapon he uses
     * @param learningRate - how strong he updates weights
     */
    public Combatant(String name, Weapon weapon, Double learningRate) {
        this.name = name;
        this.hitPoints = 100;
        this.weapon = weapon;
        this.statesList = new ArrayList<>();
        this.combatantMind = new NeuralNetwork(27, 30, 16);
        this.combatantMind.getNodes().forEach(node -> node.setLearningRate(learningRate));
    }

    /**
     *
     * @param enemyStates - states of the enemy
     * @param enemyDistance - distance to enemy
     * @param enemyWeapon - weapon of the enemy
     */
    public void setStates(List<States> enemyStates, Integer enemyDistance, Weapon enemyWeapon) {
        Map<Long, Double> input = new HashMap<>();
        input.put(0L, statesList.contains(States.ABOVE) ? 1.0 : 0.0);
        input.put(1L, statesList.contains(States.TURNED) ? 1.0 : 0.0);
        input.put(2L, statesList.contains(States.CROUCHED) ? 1.0 : 0.0);
        input.put(3L, statesList.contains(States.KNOCKED) ? 1.0 : 0.0);
        input.put(4L, statesList.contains(States.WEAPON_SIDE) ? 1.0 : 0.0);
        input.put(5L, statesList.contains(States.WEAPON_EXTENDED) ? 1.0 : 0.0);
        input.put(6L, statesList.contains(States.WEAPON_LOW) ? 1.0 : 0.0);
        input.put(7L, statesList.contains(States.WEAPON_HIGH) ? 1.0 : 0.0);
        input.put(8L, statesList.contains(States.STAGGERED) ? 1.0 : 0.0);
        input.put(9L, weapon.getLength() * 1.0);
        input.put(10L, weapon.getEfficiencies().get(DamageTypes.PIERCE) * 1.0);
        input.put(11L, weapon.getEfficiencies().get(DamageTypes.BLUNT) * 1.0);
        input.put(12L, weapon.getEfficiencies().get(DamageTypes.SLASH) * 1.0);
        input.put(13L, enemyDistance * 1.0);
        input.put(14L, enemyStates.contains(States.ABOVE) ? 1.0 : 0.0);
        input.put(15L, enemyStates.contains(States.TURNED) ? 1.0 : 0.0);
        input.put(16L, enemyStates.contains(States.CROUCHED) ? 1.0 : 0.0);
        input.put(17L, enemyStates.contains(States.KNOCKED) ? 1.0 : 0.0);
        input.put(18L, enemyStates.contains(States.WEAPON_SIDE) ? 1.0 : 0.0);
        input.put(19L, enemyStates.contains(States.WEAPON_EXTENDED) ? 1.0 : 0.0);
        input.put(20L, enemyStates.contains(States.WEAPON_LOW) ? 1.0 : 0.0);
        input.put(21L, enemyStates.contains(States.WEAPON_HIGH) ? 1.0 : 0.0);
        input.put(22L, enemyStates.contains(States.STAGGERED) ? 1.0 : 0.0);
        input.put(23L, enemyWeapon.getLength() * 1.0);
        input.put(24L, enemyWeapon.getEfficiencies().get(DamageTypes.PIERCE) * 1.0);
        input.put(25L, enemyWeapon.getEfficiencies().get(DamageTypes.BLUNT) * 1.0);
        input.put(26L, enemyWeapon.getEfficiencies().get(DamageTypes.SLASH) * 1.0);

        combatantMind.setValues(input);
    }

    private MoveTypes getIdeaWithWeights() {
        combatantMind.calculateFullPass();
        Map<Double, MoveTypes> ideas = new HashMap<>();

        MoveTypes.getAll()
                .forEach(
                        type -> ideas.put(
                                combatantMind.getNodeById(outputStart + type.getId()).value,
                                type
                        )
                );

        List<Double> weights = new ArrayList<>(ideas.keySet());
        Double lowestWeight = Collections.min(weights);
        weights = weights.stream()
                .map(w -> w - lowestWeight)
                .collect(Collectors.toList());

        weights.sort(Comparator.naturalOrder());
        Double total = weights.stream().mapToDouble(i -> i).sum();
        Double rando = Math.random() * total;
        MoveTypes best = MoveTypes.WAIT;

        for (int i = 0; i < weights.size(); i++) {
            if (rando < weights.get(i)) {
                best = ideas.get(weights.get(i) + lowestWeight);
                break;
            }
            rando -= weights.get(i);
        }
        return best;
    }

    public void pickMove() {
        MoveTypes choosen = getIdeaWithWeights();
        move = weapon.getOptionByType(choosen);
        wantedMove = weapon.getOptionByType(choosen);

        ArrayList<States> currentStates = new ArrayList<>(this.statesList);
        currentStates.retainAll(move.getUnavailableOn());

        if (!currentStates.isEmpty())
            move = weapon.getOptionByType(MoveTypes.WAIT);
    }

    public void learn(Effect myEffect, Double enemyDamage, Integer distance) {
        if (!wantedMove.getType().name().equals(move.getType().name())) {
            combatantMind.expectValues(Map.of(wantedMove.getType().getId() + outputStart, 0.0));
        }

        combatantMind.expectValues(Map.of(move.getType().getId() + outputStart, gradeMove(myEffect, enemyDamage, distance)));
        combatantMind.recalculateFullPass();
        combatantMind.updateWeights();
    }

    private Double gradeMove(Effect myEffect, Double enemyDamage, Integer distance) {
        if (move.getType() == MoveTypes.CLOSE_IN) {
            if (distance > weapon.getLength())
                return 1.0;
            if (distance < weapon.getLength())
                return 0.0;
        }

        if (myEffect == Effect.HIT || myEffect == Effect.CRIT){
            if (damageDealt(myEffect) >= enemyDamage) return 1.0;
            else return 0.5;
        }

        if (myEffect == Effect.PARRY){
            if (enemyDamage > 0.0) return 0.0;
            else return 1.0;
        }

        if (myEffect == Effect.MISS) return 0.0;

        return 0.0;
    }

    public Combatant healUp() {
        this.hitPoints = 100;
        this.statesList = new ArrayList<>();
        return this;
    }

    public Double damageDealt(Effect effect) {
        switch (effect) {
            case CRIT:
                return 20.0 * this.weapon.getEfficiencies().get(this.move.getDamageType());
            case HIT:
                return 10.0 * this.weapon.getEfficiencies().get(this.move.getDamageType());
            case PARRY:
                return 0.0;
            case MISS:
                return 0.0;
        }
        return 0.0;
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

    @Override
    public boolean equals(Object obj) {
        return ((Combatant) obj).name.equals(this.name);
    }
}
