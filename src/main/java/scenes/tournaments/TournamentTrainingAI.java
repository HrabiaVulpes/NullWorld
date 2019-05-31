package scenes.tournaments;

import agent.Player;
import scenes.duels.TrainingAI;

import java.util.ArrayList;
import java.util.List;

public class TournamentTrainingAI extends TournamentBase {
    protected List<Player> learningCombatants = new ArrayList<>();

    public TournamentTrainingAI() {
    }

    public TournamentTrainingAI(List<Player> players){
        super(players);
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
}
