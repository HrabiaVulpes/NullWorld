package agent;

import combat_data.*;
import scenes.HumanVsAi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Player {

    public String name;
    public Integer hitPoints;
    public Weapon weapon;
    public List<States> statesList;
    public Move move;
    public Integer victoriesCount = 0;
    public Integer lossesCount = 0;

    public Player() {
    }

    /**
     * @param name   - name of combatant
     * @param weapon - weapon he uses
     */
    public Player(String name, Weapon weapon) {
        this.name = name;
        this.hitPoints = 100;
        this.weapon = weapon;
        this.statesList = new ArrayList<>();
    }

    public Player healUp() {
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

    public void pickMove() {
        Scanner scan = new Scanner(System.in);
        String chosenMove = scan.nextLine();
        do {
            move = weapon.getOptionByType(MoveTypes.valueOf(chosenMove));
            if(!Arrays.asList(MoveTypes.values()).contains(MoveTypes.valueOf(chosenMove)))
                System.out.println("This move is not possible try again");
        } while(!Arrays.asList(MoveTypes.values()).contains(MoveTypes.valueOf(chosenMove)));
    }

    public void fightForRounds(int rounds) {
        HumanVsAi fight = new HumanVsAi(new Player(), new Combatant());
        for (int i = 0; i < rounds; i++) {

            fight.processTurn();
        }
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
