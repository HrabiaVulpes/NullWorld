package scenes.tournaments;

import agent.Player;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class TournamentBase {
    protected List<Player> players = new ArrayList<>();

    public TournamentBase() {
    }

    public TournamentBase(List<Player> players) {
        this.players = players;
    }

    public abstract void runRound(int roundLength);

    public void runTournament(int rounds, int roundLenght) {
        for (int i = 0; i < rounds; i++) {
            runRound(roundLenght);
        }

        System.out.println("Time for a scoreboard!");
        players.sort(Comparator.comparing(player -> player.victoriesCount));
        players.forEach(
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

    protected Player randomCombatant() {
        long choosen = (Math.round(Math.random() * players.size())) % players.size();
        return players.get((int) choosen);
    }

    protected void jsonWriting() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/combatants.json"), players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void resetWins() {
        players.forEach(combatant -> combatant.victoriesCount = 0);
        players.forEach(combatant -> combatant.lossesCount = 0);
    }
}
