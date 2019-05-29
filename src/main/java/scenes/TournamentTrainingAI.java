package scenes;

import agent.Player;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TournamentTrainingAI {
    protected List<Player> learningCombatants = new ArrayList<>();

    public TournamentTrainingAI() {
    }

    public TournamentTrainingAI(List<Player> learningCombatants) {
        this.learningCombatants = learningCombatants;
    }

    public void runRound(int roundLenght) {
        Player player1 = randomCombatant();
        Player player2;
        do {
            player2 = randomCombatant();
        } while (player1.name.equals(player2.name));

        TrainingAI training = new TrainingAI(player1, player2);
        System.out.println(training);
        training.fightForRounds(roundLenght);
        if (training.winner() != null) {
            training.winner().victoriesCount++;
            training.loser().lossesCount++;
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
        learningCombatants.sort(Comparator.comparing(player -> player.victoriesCount));
        learningCombatants.forEach(
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
        long choosen = (Math.round(Math.random() * learningCombatants.size())) % learningCombatants.size();
        return learningCombatants.get((int) choosen);
    }

    protected void jsonWriting() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/combatants.json"), learningCombatants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void resetWins() {
        learningCombatants.forEach(combatant -> combatant.victoriesCount = 0);
        learningCombatants.forEach(combatant -> combatant.lossesCount = 0);
    }
}
