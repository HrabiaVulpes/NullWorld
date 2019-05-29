package userInterface;

import agent.LearningCombatant;
import agent.Player;
import scenes.HumanVsAi;

import static combat_data.ObjectsLists.getData;
import static userInterface.ConsoleUtils.*;

public class Main {




    public static void main(String[] args) {
        hello();
        Player player = new Player(setName("your"), getData().findWeaponByName(chooseWeapon("your")));
        weaponStats("your", player.weapon);
        LearningCombatant learningCombatant = new LearningCombatant(setName("computer"), getData().findWeaponByName(chooseWeapon("opponent")));
        HumanVsAi duel = new HumanVsAi(player, learningCombatant);
        duel.fightForRounds(50);
        showWinner(duel.winner());
    }
}
