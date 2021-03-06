package scenes.tournaments;

import agent.Player;
import scenes.duels.FightAI;

import java.util.Comparator;
import java.util.List;

public class Versus extends TournamentBase {
    private List<Player> guests;

    public Versus(List<Player> house, List<Player> guests) {
        super(house);
        this.guests = guests;
    }

    private Player randomEnemyCombatant() {
        long choosen = (Math.round(Math.random() * guests.size())) % guests.size();
        return guests.get((int) choosen);
    }

    public void runRound(int roundLenght) {
        Player player1 = randomCombatant();
        Player player2 = randomEnemyCombatant();

        FightAI training = new FightAI(player1, player2);
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
        players.sort(Comparator.comparing(player -> player.victoriesCount));
        players.forEach(
                player -> System.out.println(player.name + "\t"
                        + player.victoriesCount + "-" + player.lossesCount + "\t" +
                        "(" + player.weapon.getName() + ")")
        );

        System.out.println("And a summary:");
        System.out.println("House: "
                + players.stream().mapToInt(Player::getVictoriesCount).sum()
                + " Guests: "
                + guests.stream().mapToInt(Player::getVictoriesCount).sum()
        );
    }
}
