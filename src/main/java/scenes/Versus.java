package scenes;

import agent.Combatant;

import java.util.Comparator;
import java.util.List;

public class Versus extends Tournament {
    private List<Combatant> enemy_combatants;

    public Versus(List<Combatant> combatants, List<Combatant> enemies){
        super(combatants);
        this.enemy_combatants = enemies;
    }

    private Combatant randomEnemyCombatant() {
        long choosen = (Math.round(Math.random() * enemy_combatants.size())) % enemy_combatants.size();
        return enemy_combatants.get((int) choosen);
    }

    public void runRound(int roundLenght) {
        Combatant player1 = randomCombatant();
        Combatant player2 = randomEnemyCombatant();

        Fight training = new Fight(player1, player2);
        System.out.println(training);
        training.fightForRounds(roundLenght);
        if (training.winner() != null) {
//            training.winner().victoriesCount++;
//            training.looser().lossesCount++;
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

        System.out.println("And a summary:");
        System.out.println("House: "
                + combatants.stream().mapToInt(Combatant::getVictoriesCount).sum()
                + " Guests: "
                + enemy_combatants.stream().mapToInt(Combatant::getVictoriesCount).sum()
        );
    }
}
