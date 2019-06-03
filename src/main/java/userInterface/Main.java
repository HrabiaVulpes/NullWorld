package userInterface;

import agent.LearningCombatant;
import agent.Player;
import scenes.duels.HumanVsAi;

import static combat_data.ObjectsLists.getData;
import static userInterface.ConsoleUtils.*;

public class Main {
    public static void main(String[] args) {
        hello();
        Player player = new Player(
                setName("your"),
                getData().findWeaponByName(chooseWeapon("your"))
        );
        weaponStats("your", player.weapon);

        String opponentWeapon = chooseWeapon("opponent");
        Player enemy = getData().combatantsList.stream()
                .filter(fighter -> fighter.whoControl.contains("AI"))
                .filter(fighter -> fighter.weapon.getName().equals(opponentWeapon))
                .findFirst()
                .orElse(new LearningCombatant(
                        setName("computer"),
                        getData().findWeaponByName(opponentWeapon)
                ));
        HumanVsAi duel = new HumanVsAi(player, enemy);
        duel.fightForRounds(50);
        showWinner(duel.winner());
    }
}
