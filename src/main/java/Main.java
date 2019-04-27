import agent.Combatant;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.ObjectsLists;
import combat_data.States;
import scenes.Tournament;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


        List<Combatant> combatants = ObjectsLists.getData()
                .weaponList.stream()
                .map(weapon -> new Combatant(names.get(weapon.getName()), weapon))
                .collect(Collectors.toList());
        Tournament tournament = new Tournament(ObjectsLists.getData().combatantsList);
        tournament.runTournament(100, 50);
    }

    public static void change(){
        ObjectsLists.getData().weaponList.forEach(
                weapon -> weapon.getOptions().stream()
                        .filter(move -> !Arrays.asList(JUMP, DODGE, DUCK, CLOSE_IN, BACK_AWAY, GET_UP).contains(move.getType()))
                        .forEach(move -> move.getUnavailableOn().add(States.STAGGERED))
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/weapons.json"), ObjectsLists.getData().weaponList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

}