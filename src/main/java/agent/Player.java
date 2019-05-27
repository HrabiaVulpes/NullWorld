package agent;

import combat_data.*;
import scenes.HumanVsAi;

import java.util.*;
import java.util.stream.Collectors;

import static userInterface.ConsoleUtils.chosenMove;

public class Player {

    public String whoControl;
    public String name;
    public Integer hitPoints;
    public Weapon weapon;
    public Set<States> statesList;
    public Move move;
    public Integer victoriesCount = 0;
    public Integer lossesCount = 0;
    public Integer maxHP = 100;
    ArrayList<MoveTypes> availableMoves = new ArrayList();

    public Player() {
    }

    /**
     * @param name   - name of combatant
     * @param weapon - weapon he uses
     */
    public Player(String name, Weapon weapon) {
        this.name = name;
        this.hitPoints = maxHP;
        this.weapon = weapon;
        this.statesList = new HashSet<>();
        whoControl = "Player";
    }

    public Player healUp() {
        this.hitPoints = maxHP;
        this.statesList = new HashSet<>();
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

    public void pickMove() {
        String chosenMove;
        do {
            chosenMove = chosenMove();
            if (!Arrays.stream(MoveTypes.values())
                    .map(Enum::toString)
                    .collect(Collectors.toList()).contains(chosenMove))
                System.out.println("This move is not possible try again");
        } while (!Arrays.stream(MoveTypes.values())
                .map(Enum::toString)
                .collect(Collectors.toList()).contains(chosenMove));
        move = weapon.getOptionByType(MoveTypes.valueOf(chosenMove));
    }

    public ArrayList getAvailableMoves(){
        availableMoves.clear();
        availableMoves.addAll(weapon.getOptions().stream().map(Move::getType).collect(Collectors.toList()));
        for(MoveTypes moveType : availableMoves){
            Move testedMove = new Move(moveType);
            for(States states: statesList) {
                if (testedMove.getUnavailableOn().contains(states))
                    availableMoves.remove(testedMove);
            }
        }
        return availableMoves;
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

    public Collection<States> getStatesList() {
        return statesList;
    }

    public void setStatesList(Collection<States> statesList) {
        this.statesList = new HashSet<>(statesList);
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
    public String toString(){
        return this.name + " my weapon: " + this.weapon.getName();
    }
}
