import agent.Combatant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.Weapon;
import scenes.Duel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Weapon> shivs = null;

    public static void main(String[] args) {
        jsonLoading();

        Duel duel = new Duel(
                new Combatant("Red", shivs.get(1)),
                new Combatant("Blue", shivs.get(1))
        );

        for (int i = 0; i < 1000; i++) {
            duel.processTurn();
        }
    }

    private static void jsonLoading() {
        List<Weapon> weaponList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/dagger.json"), weaponList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = Main.class.getClassLoader().getResource("weapons.json").getFile();

//        System.out.println(filePath);
        try {
            shivs = objectMapper.readValue(new File(filePath), new TypeReference<List<Weapon>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert shivs != null;
        shivs.forEach(weapon -> System.out.println(weapon.getName()));
    }

}