import combat_data.ObjectsLists;
import scenes.Tournament;

import java.util.Arrays;
import java.util.List;

public class Main {
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
        Tournament tournament = new Tournament(ObjectsLists.getData().weaponList.subList(1, 2), names);
        tournament.eternalTournament(1000, 100);
    }

}