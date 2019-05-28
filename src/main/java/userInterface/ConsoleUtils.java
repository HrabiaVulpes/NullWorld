package userInterface;

import agent.Player;
import combat_data.Weapon;

import java.util.Scanner;
import java.util.stream.Collectors;

import static combat_data.ObjectsLists.getData;

public class ConsoleUtils {
    public static void hello() {
        System.out.println("Hello!! ");
    }

    public static String setName(String player) {
        String name;
        Scanner scan = new Scanner(System.in);
        System.out.println("Write " + player + " fighter name:");
        name = scan.nextLine();
        System.out.println("Name: " + name);
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
        return weapon;
    }

    public static void weaponStats(String player, Weapon weapon) {
        System.out.println(player + " weapon is:\n" + weapon.getName() + " length: " + weapon.length);
        weapon.efficiencies.keySet().forEach(efficiency ->
                System.out.println(efficiency + ": " + weapon.efficiencies.get(efficiency)));
    }

    private static boolean containsWeaponOnList(String weaponName) {
        return getData().weaponList.stream()
                .map(Weapon::getName)
                .collect(Collectors.toList())
                .contains(weaponName);
    }

    public static String chosenMove() {
        String move;
        Scanner scan = new Scanner(System.in);
        move = scan.nextLine().toUpperCase();
        return move;
    }

    public static void showStatus(Player player) {
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.println(player.whoControl + "\t" + player.name +
                "\t HP: " + player.hitPoints + "/" + player.maxHP +
                " \t Weapon: " + player.weapon.getName());
        System.out.println("Status: ");
        player.statesList.forEach(states -> System.out.println(states));
        //TODO divine for body and weapon status

    }

    public static void showDistance(int distance) {
        System.out.println("Distance: " + distance);
    }

    public static void availableMoves(Player player) {
        System.out.print("Available moves: \n");
        player.getAvailableMoves().forEach(move -> System.out.print(move + " "));
        System.out.println("\n---------------------------------------------------------------------------------------------------------------- \n" +
                "Your next move:_");
    }

    public static void showWinner(Player winner) {
        System.out.println("MATCH END! ");
        if (winner != null)
            System.out.println("WINNER:\n" + winner.name);
        else System.out.println("DRAW");
    }
}
