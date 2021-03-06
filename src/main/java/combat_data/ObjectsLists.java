package combat_data;

import agent.LearningCombatant;
import agent.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ObjectsLists {
    static ObjectsLists data = null;

    public List<Weapon> weaponList;
    public List<Player> combatantsList;
    public List<Player> oldCombatantsList;

    public static ObjectsLists getData() {
        if (data == null) {
            data = new ObjectsLists();
        }
        return data;
    }

    private ObjectsLists() {
        loadWeapons();
        loadCombatants();
//        loadOldCombatants();
    }

    private void loadWeapons() {
        String filePath = ObjectsLists.class.getClassLoader().getResource("weapons.json").getFile();
        try {
            weaponList = new ObjectMapper().readValue(new File(filePath), new TypeReference<List<Weapon>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert weaponList != null;
    }

    private void loadCombatants() {
        String filePath = ObjectsLists.class.getClassLoader().getResource("combatants.json").getFile();
        try {
            combatantsList = new ObjectMapper().readValue(new File(filePath), new TypeReference<List<LearningCombatant>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert weaponList != null;
    }

    private void loadOldCombatants() {
        String filePath = ObjectsLists.class.getClassLoader().getResource("combatants_old.json").getFile();
        try {
            oldCombatantsList = new ObjectMapper().readValue(new File(filePath), new TypeReference<List<LearningCombatant>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert weaponList != null;
    }

    public Weapon findWeaponByName(String name) {
        Weapon chosenWeapon = null;
        for (Weapon weapon : getData().weaponList) {
            if (weapon.getName().equals(name))
                chosenWeapon = weapon;
        }
        return chosenWeapon;
    }
}
