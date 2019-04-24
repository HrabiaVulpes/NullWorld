import agent.Combatant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.Weapon;
import scenes.Duel;
import scenes.Tournament;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static List<Weapon> availableWeapons = null;
    private static List<String> names = Arrays.asList(
            "REED",
            "BLUE",
            "GREEN",
            "CYAN",
            "PINK",
            "PURPLE",
            "VIOLET",
            "BLACK",
            "WHITE"
    );

    public static void main(String[] args) {
        loadWeapons();
        Tournament tournament = new Tournament(availableWeapons, names);
        tournament.runTournament(1000, 50);
    }

    private static void loadWeapons(){
        ObjectMapper objectMapper = new ObjectMapper();
        String filePath = Main.class.getClassLoader().getResource("weapons.json").getFile();
        try {
            availableWeapons = objectMapper.readValue(new File(filePath), new TypeReference<List<Weapon>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert availableWeapons != null;
        availableWeapons.forEach(weapon -> System.out.println(weapon.getName()));
    }

    private static void jsonLoading() {
        List<Weapon> weaponList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/dagger.json"), weaponList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}