package scenes;

import agent.Combatant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tournament {
    protected List<Combatant> combatants = new ArrayList<>();

    public Tournament() {
    }

    public Tournament(List<Combatant> combatants){
        this.combatants = combatants;
    }

    public void runRound(int roundLenght) {
        Combatant player1 = randomCombatant();
        Combatant player2;
        do {
            player2 = randomCombatant();
        } while (player1.name.equals(player2.name));

        Training training = new Training(player1, player2);
        System.out.println(training);
        training.fightForRounds(roundLenght);
        if (training.winner() != null) {
            training.winner().victoriesCount++;
            training.looser().lossesCount++;
            System.out.println(training.winner().name + " won!\n\n");
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

    protected Combatant randomCombatant() {
        long choosen = (Math.round(Math.random() * combatants.size())) % combatants.size();
        return combatants.get((int) choosen);
    }

    protected void jsonWriting() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/combatants.json"), combatants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void resetWins(){
        combatants.forEach(combatant -> combatant.victoriesCount = 0);
        combatants.forEach(combatant -> combatant.lossesCount = 0);
    }
}
