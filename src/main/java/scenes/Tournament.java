package scenes;

import agent.Combatant;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.Weapon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Tournament {
    private List<Weapon> availableWeapons = null;
    private List<Combatant> combatants = new ArrayList<>();

    public Tournament(List<Weapon> availableWeapons, List<String> names) {
        this.availableWeapons = new ArrayList<>(availableWeapons);
        names.forEach(
                name -> combatants.add(newContender(name))
        );
    }

    public Tournament(List<Combatant> combatants){
        this.combatants = combatants;
        availableWeapons = combatants.stream().map(Combatant::getWeapon).distinct().collect(Collectors.toList());
    }

    public void runRound(int roundLenght) {
        Combatant player1 = randomCombatant();
        Combatant player2;
        do {
            player2 = randomCombatant();
        } while (player1.name.equals(player2.name));

        Duel duel = new Duel(player1, player2);
        System.out.println(duel);
        duel.fightForRounds(roundLenght);
        if (duel.winner() != null) {
            duel.winner().victoriesCount++;
            duel.looser().lossesCount++;
            System.out.println(duel.winner().name + " won!\n\n");
        } else System.out.println("It's a draw!\n\n");
        player1.healUp();
        player2.healUp();
    }

    public void runTournament(int rounds, int roundLenght) {
        for (int i = 0; i < rounds; i++) {
            runRound(roundLenght);
        }

        System.out.println("Time for a scoreboard!");
        combatants.sort(Comparator.comparing(player -> player.victoriesCount));
        combatants.forEach(
                player -> System.out.println(player.name + "\t"
                        + player.victoriesCount + "-" + player.lossesCount + "\t" +
                        "(" + player.weapon.getName() + ")")
        );
    }

    public void eternalTournament(int rounds, int roundLenght) {
        while (true) {
            runTournament(rounds, roundLenght);
            resetWins();
            jsonWriting();
        }
    }

    private Weapon randomWeapon() {
        long choosen = (Math.round(Math.random() * availableWeapons.size())) % availableWeapons.size();
        return availableWeapons.get((int) choosen);
    }

    private Combatant randomCombatant() {
        long choosen = (Math.round(Math.random() * combatants.size())) % combatants.size();
        return combatants.get((int) choosen);
    }

    private Combatant newContender(String name) {
        return new Combatant(name, randomWeapon());
    }

    private void jsonWriting() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/combatants.json"), combatants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetWins(){
        combatants.forEach(combatant -> combatant.victoriesCount = 0);
        combatants.forEach(combatant -> combatant.lossesCount = 0);
    }
}
