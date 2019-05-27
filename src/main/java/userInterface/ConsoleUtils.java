package userInterface;

import combat_data.Weapon;

import java.util.Scanner;
import java.util.stream.Collectors;

import static combat_data.ObjectsLists.getData;

public class ConsoleUtils {
    public static void hello() {
        System.out.println("Hello!! ");
    }

    public static String setName() {
        String name;
        Scanner scan = new Scanner(System.in);
        System.out.println("Write your fighter name:");
        name = scan.nextLine();
        System.out.println("Name: "+name);
        return name;

    }

    public static String chooseWeapon(String player) {
        System.out.println("\nWeapon list: \n");
        getData().weaponList.forEach(weapon -> System.out.println(weapon.getName()));

        System.out.println("\nChoose " + player + " weapon from list:");

        Scanner scan = new Scanner(System.in);
        String weapon;
        do {
            weapon = scan.nextLine().toUpperCase();
            if (!containsWeaponOnList(weapon)) System.out.println("No such weapon");
        }
        while (!containsWeaponOnList(weapon));
        System.out.println("Your weapon is: " + weapon);
        return weapon;
    }

    static boolean containsWeaponOnList(String weaponName) {
        return getData().weaponList.stream()
                .map(Weapon::getName)
                .collect(Collectors.toList())
                .contains(weaponName);
    }

    public static String chosenMove(){
        String move;
        Scanner scan = new Scanner(System.in);
        move = scan.nextLine().toUpperCase();
        return move;
    }
}
