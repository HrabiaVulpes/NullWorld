package scenes.tournaments;

import agent.GeneticallyLearningCombatant;
import agent.Player;
import scenes.duels.FightAI;

import java.util.Comparator;
import java.util.List;

public class GeneticallyLearningTournament extends TournamentBase {
    public GeneticallyLearningTournament(List<Player> learningCombatants) {
        super(learningCombatants);
    }

    @Override
    public void runRound(int roundLength) {
        GeneticallyLearningCombatant player1 = (GeneticallyLearningCombatant) randomCombatant();
        GeneticallyLearningCombatant player2;
        do {
            player2 = (GeneticallyLearningCombatant) randomCombatant();
        } while (player1.getID() == player2.getID());

        FightAI fight = new FightAI(player1, player2);
        System.out.println(fight);
        fight.fightForRounds(roundLength);
        if (fight.winner() != null) {
            Player looser = fight.loser();
            System.out.println(fight.winner().name + " won!\n\n");

            player1.healUp();
            player2.healUp();
            players.remove(looser);
        } else {
            System.out.println("It's a draw!\n\n");
            players.remove(Math.random() > 0.5 ? player1 : player2);
        }
    }

    public void runTournament(int rounds, int roundLength) {
        for (int i = 0; i < rounds; i++) {
            while (players.size() > 3) {
                runRound(roundLength);
            }

            System.out.println("Time for survivors!");
            players.sort(Comparator.comparing(player -> player.victoriesCount));
            players.forEach(
                    player -> System.out.println(player.name + "\t"
                            + "(" + player.weapon.getName() + ")")
            );

            GeneticallyLearningCombatant victor1 = (GeneticallyLearningCombatant) players.get(0);
            GeneticallyLearningCombatant victor2 = (GeneticallyLearningCombatant) players.get(1);
            GeneticallyLearningCombatant victor3 = (GeneticallyLearningCombatant) players.get(2);

            while (players.size() < 30) {
                players.add(victor1.mutate(getLastId()));
                players.add(victor2.mutate(getLastId()));
                players.add(victor3.mutate(getLastId()));
            }
        }
    }

    Long getLastId() {
        return players.stream()
                .map(player -> ((GeneticallyLearningCombatant) player).getID())
                .mapToLong(p -> p)
                .max()
                .orElse(0L) + 1;
    }
}
