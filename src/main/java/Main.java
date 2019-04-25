import agent.Combatant;
import combat_data.ObjectsLists;
import scenes.Tournament;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static Map<String, String> names = new HashMap<>();

    public static void main(String[] args) {
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

}