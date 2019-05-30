import agent.LearningCombatant;
import agent.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.ObjectsLists;
import combat_data.States;
import scenes.tournaments.TournamentTrainingAI;
import scenes.tournaments.Versus;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static combat_data.MoveTypes.*;

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

        List<Player> learningCombatants = ObjectsLists.getData()
                .weaponList.stream()
                .map(weapon -> new LearningCombatant("Red" + names.get(weapon.getName()), weapon))
                .collect(Collectors.toList());
        learningCombatants.addAll(ObjectsLists.getData()
                .weaponList.stream()
                .map(weapon -> new LearningCombatant("Blue" + names.get(weapon.getName()), weapon))
                .collect(Collectors.toList()));
        learningCombatants.addAll(ObjectsLists.getData()
                .weaponList.stream()
                .map(weapon -> new LearningCombatant("Green" + names.get(weapon.getName()), weapon))
                .collect(Collectors.toList()));

        TournamentTrainingAI tournamentTrainingAI = new TournamentTrainingAI(learningCombatants);
        tournamentTrainingAI.eternalTournament(1000, 30);
    }

    private static void swordTournament() {
        names.put("KATANA", "Samurai");
        names.put("FIST", "Yang");
        names.put("SCYTHE", "Ruby Rose");

        List<Player> swordsmen_red = names.values().stream()
                .map(name -> new LearningCombatant("Red " + name, ObjectsLists.getData().weaponList.get(1)))
                .collect(Collectors.toList());

        List<Player> swordsmen_blue = new ArrayList<>(ObjectsLists.getData().combatantsList);

        TournamentTrainingAI tournamentTrainingAI = new Versus(swordsmen_blue, swordsmen_red);
        tournamentTrainingAI.runTournament(1000, 30);
    }

    public static void change() {
        ObjectsLists.getData().weaponList.forEach(
                weapon -> weapon.getOptions().stream()
                        .filter(move -> Arrays.asList(OVERHEAD, SLASH, KICK, THRUST, UNDERSTRIKE, BACK_AWAY).contains(move.getType()))
                        .forEach(move -> move.getUnavailableOn().add(States.KNOCKED))
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/weapons.json"), ObjectsLists.getData().weaponList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runTournament();
    }

}