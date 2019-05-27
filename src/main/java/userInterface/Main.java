package userInterface;

import agent.Player;
import combat_data.ObjectsLists;

import static combat_data.ObjectsLists.getData;
import static userInterface.ConsoleUtils.*;

public class Main {




    public static void main(String[] args) {
        hello();
        //setName();
        Player player = new Player(setName(), getData().findWeaponByName(chooseWeapon("your")));
        System.out.println(player);
//        Weapon weapon = ObjectsLists.getData()
//                .weaponList.
//        HumanVsAi fight = new HumanVsAi(new Player(),  new Combatant("Red" + names.get(weapon.getName()), weapon));

        System.out.println(setName() + chooseWeapon("your"));
        //chooseWeapon("your");
//        fight.fightForRounds(5);


    }
}
