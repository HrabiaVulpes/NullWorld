package combat_data;

import agent.Combatant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ObjectsLists {
    static ObjectsLists data = null;

    public List<Weapon> weaponList;
    private List<Combatant> combatantsList;

    public static ObjectsLists getData(){
        if (data == null){
            data = new ObjectsLists();
        }
        return data;
    }

    private ObjectsLists(){
        loadWeapons();
        loadCombatants();
    }

    private void loadWeapons(){
        String filePath = ObjectsLists.class.getClassLoader().getResource("weapons.json").getFile();
        try {
            weaponList = new ObjectMapper().readValue(new File(filePath), new TypeReference<List<Weapon>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert weaponList != null;
    }

    private void loadCombatants(){
        String filePath = ObjectsLists.class.getClassLoader().getResource("combatants.json").getFile();
        try {
            combatantsList = new ObjectMapper().readValue(new File(filePath), new TypeReference<List<Combatant>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert weaponList != null;
    }
}
