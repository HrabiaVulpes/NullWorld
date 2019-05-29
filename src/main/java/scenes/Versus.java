package scenes;

import agent.LearningCombatant;

import java.util.Comparator;
import java.util.List;

public class Versus extends TournamentTrainingAI {
    private List<LearningCombatant> enemy_Learning_combatants;

    public Versus(List<LearningCombatant> learningCombatants, List<LearningCombatant> enemies){
        super(learningCombatants);
        this.enemy_Learning_combatants = enemies;
    }

    private LearningCombatant randomEnemyCombatant() {
        long choosen = (Math.round(Math.random() * enemy_Learning_combatants.size())) % enemy_Learning_combatants.size();
        return enemy_Learning_combatants.get((int) choosen);
    }

    public void runRound(int roundLenght) {
        LearningCombatant player1 = randomCombatant();
        LearningCombatant player2 = randomEnemyCombatant();

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
        learningCombatants.sort(Comparator.comparing(player -> player.victoriesCount));
        learningCombatants.forEach(
                player -> System.out.println(player.name + "\t"
                        + player.victoriesCount + "-" + player.lossesCount + "\t" +
                        "(" + player.weapon.getName() + ")")
        );

        System.out.println("And a summary:");
        System.out.println("House: "
                + learningCombatants.stream().mapToInt(LearningCombatant::getVictoriesCount).sum()
                + " Guests: "
                + enemy_Learning_combatants.stream().mapToInt(LearningCombatant::getVictoriesCount).sum()
        );
    }
}
