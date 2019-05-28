package userInterface;

import agent.Combatant;
import agent.Player;
import combat_data.Move;
import combat_data.MoveTypes;
import scenes.HumanVsAi;

import static combat_data.ObjectsLists.getData;
import static userInterface.ConsoleUtils.*;

public class Main {




    public static void main(String[] args) {
        hello();
        //setName();
        Player player = new Player(setName("your"), getData().findWeaponByName(chooseWeapon("your")));
        weaponStats("your", player.weapon);
        Combatant combatant = new Combatant(setName("computer"), getData().findWeaponByName(chooseWeapon("opponent")));
        HumanVsAi duel = new HumanVsAi(player, combatant);
        duel.fightForRounds(50);
        showWinner(duel.winner());


//        System.out.println(getData().weaponList.get(5).getName());
//        getData().weaponList.get(5).getOptions().forEach(w-> {
//                    System.out.println("\n"+w.getType() +"\n");
//                    w.getUnavailableOn().forEach(m->System.out.println(m.name()));
//                }
//        );



    }
}
