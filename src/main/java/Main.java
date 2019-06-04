import agent.LearningCombatant;
import agent.Player;
import agent.simpleAIAgent.ForeverBest;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.*;
import scenes.tournaments.FairTournamentTrainingAI;
import scenes.tournaments.TournamentBase;
import scenes.tournaments.TournamentTrainingAI;
import scenes.tournaments.Versus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static combat_data.MoveTypes.BLOCK;
import static combat_data.MoveTypes.CLOSE_IN;

public class Main {
    private static Map<String, String> names = new HashMap<>();

    private static void runTournament() {
        names.put("DAGGER", "Thief");
        names.put("SWORD", "Ashen");
        names.put("GREAT_SWORD", "Knight");
        names.put("ULTRA_GREAT_SWORD", "Heavy");
        names.put("CURVED_SWORD", "Ali Baba");
        names.put("KATANA", "Samurai");
        names.put("CURVED_GREAT_SWORD", "Daimyo");
        names.put("PIERCING_SWORD", "Fencer");
        names.put("AXE", "Woodcutter");
        names.put("GREAT_AXE", "Barbarian");
        names.put("HAMMER", "Drang");
        names.put("GREAT_HAMMER", "Smoug");
        names.put("FIST", "Yang");
        names.put("SPEAR", "Lancer");
        names.put("HALBERD", "Hou Yi");
        names.put("SCYTHE", "Ruby Rose");

        List<Player> learningCombatants = new ArrayList<>();

        for (String weapon : names.keySet()){
            learningCombatants.add(
                    new LearningCombatant("Zero " + names.get(weapon),
                            ObjectsLists.getData().findWeaponByName(weapon),
                            0)
            );
        }

        for (String weapon : names.keySet()){
            learningCombatants.add(
                    new LearningCombatant("One " + names.get(weapon),
                            ObjectsLists.getData().findWeaponByName(weapon),
                            1)
            );
        }

        for (String weapon : names.keySet()){
            learningCombatants.add(
                    new LearningCombatant("Two " + names.get(weapon),
                            ObjectsLists.getData().findWeaponByName(weapon),
                            1)
            );
        }

        for (String weapon : names.keySet()){
            learningCombatants.add(
                    new ForeverBest("Simple " + names.get(weapon),
                            ObjectsLists.getData().findWeaponByName(weapon))
            );
        }

        TournamentBase tournamentTrainingAI = new TournamentTrainingAI(learningCombatants);
        tournamentTrainingAI.eternalTournament(1000, 30);
    }

    private static void fairTournament() {
        names.put("FIST", "Yang");
        names.put("HALBERD", "Hou Yi");
        names.put("SCYTHE", "Ruby Rose");

        List<Player> swordsmen_red = ObjectsLists.getData().combatantsList;
        for (String name : names.values()){
            swordsmen_red.add(new ForeverBest("Simple  " + name, ObjectsLists.getData().findWeaponByName("SWORD")));
        }

        TournamentBase tournamentTrainingAI = new FairTournamentTrainingAI(swordsmen_red);
        tournamentTrainingAI.runTournament(100, 30);
    }

    public static void change() {
        ObjectsLists.getData().weaponList.forEach(
                weapon -> weapon.getOptions().add(new Move(BLOCK)
                        .withDamage(DamageTypes.NONE)
                        .withPositionDuringMove(PositionTags.STANDARD)
                        .withRemovedStates()
                        .withAddedStates()
                        .withUnavailableOn(States.WEAPON_SIDE, States.WEAPON_EXTENDED, States.WEAPON_HIGH, States.WEAPON_LOW)
                        .withWeaponMovement(PositionTags.STANDARD, PositionTags.LOW, PositionTags.HIGH, PositionTags.SIDE, PositionTags.BACK)
                )
        );

        ObjectsLists.getData().weaponList.forEach(
                weapon -> weapon.getOptions()
                        .stream()
                        .filter(move -> move.getType().equals(CLOSE_IN))
                        .forEach(move -> move.withWeaponMovement())
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/weapons.json"), ObjectsLists.getData().weaponList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        fairTournament();
    }

}