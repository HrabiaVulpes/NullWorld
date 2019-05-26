package userInterface;
import agent.Combatant;
import agent.Player;
import scenes.HumanVsAi;

import static userInterface.PrintedFunction.*;

public class Main {

    void fightForRounds(int rounds) {
        HumanVsAi fight = new HumanVsAi(new Player(), new Combatant());
        for (int i = 0; i < rounds; i++) {

            fight.processTurn("move");
        }
    }


    public static void main(String[] args) {
        hello();
        setName();
        chooseWeapon("your");


    }
}
