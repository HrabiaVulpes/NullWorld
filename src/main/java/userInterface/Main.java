package userInterface;
import agent.Combatant;
import agent.Player;
import scenes.HumanVsAi;

import static userInterface.PrintedFunction.*;

public class Main {




    public static void main(String[] args) {
        hello();
        //setName();
        //Player player = new Player(setName(), chooseWeapon("your"));
        HumanVsAi fight = new HumanVsAi(new Player(), new Combatant());
        System.out.println(setName() + chooseWeapon("your"));
        //chooseWeapon("your");
        fight.fightForRounds(5);


    }
}
