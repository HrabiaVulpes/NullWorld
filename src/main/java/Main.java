import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.ObjectsLists;
import combat_data.Weapon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ObjectsLists.getData().weaponList.forEach(weapon -> System.out.println(weapon.getName()));
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

        System.out.println(filePath);

        List<Weapon> shivs = null;
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