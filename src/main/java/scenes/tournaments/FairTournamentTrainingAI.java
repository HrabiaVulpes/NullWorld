package scenes.tournaments;

import agent.Player;
import scenes.duels.TrainingAI;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class FairTournamentTrainingAI extends TournamentBase {
    private Map<String, List<Integer>> finalScores = new HashMap<>();

    public FairTournamentTrainingAI(List<Player> players) {
        super(players);

        players.forEach(
                combatant -> finalScores.put(combatant.name, new ArrayList<>())
        );
    }

    public Player runRound(Player player1, Player player2, int roundLength) {
        TrainingAI fight = new TrainingAI(player1, player2);
        System.out.println(fight);
        fight.fightForRounds(roundLength);
        return fight.winner();
    }

    @Override
    public void runTournament(int rounds, int roundLength) {
        for (int i = 0; i < rounds; i++) {
            runRound(roundLength);
            printToFile();
        }

        System.out.println("Time for a scoreboard!");
        players.sort(Comparator.comparing(player -> player.victoriesCount));
        players.forEach(
                player -> System.out.println(player.name + "\t"
                        + player.victoriesCount + "-" + player.lossesCount + "\t" +
                        "(" + player.weapon.getName() + ")")
        );
    }

    @Override
    public void eternalTournament(int rounds, int roundLength) {
        while (true) {
            for (int i = 0; i < rounds; i++) {
                runRound(roundLength);
            }

            players.forEach(
                    combatant -> finalScores.get(combatant.name).add(combatant.getVictoriesCount())
            );
            printToFile();
            resetWins();
            jsonWriting();
        }
    }

    private void printToFile() {
        try {
            FileWriter fw = new FileWriter(new File("target/results.txt"));
            fw.write("Scores:\n");
            for (String name : finalScores.keySet()) {
                fw.write(name + ";" + finalScores.get(name).stream().map(i -> "" + i).collect(Collectors.joining(";")));
                fw.write("\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void runRound(int roundLength) {
        for (Player p1 : players) {
            for (Player p2 : players) {
                if (!p1.name.equals(p2.name)) {
                    Map<String, Integer> victories = new HashMap<>(Map.of(p1.name, 0, p2.name, 0));
                    Player victor = runRound(p1, p2, roundLength);
                    if (victor != null) victories.put(victor.name, victories.get(victor.name) + 1);
                    p1.healUp();
                    p2.healUp();

                    victor = runRound(p1, p2, roundLength);
                    if (victor != null) victories.put(victor.name, victories.get(victor.name) + 1);
                    p1.healUp();
                    p2.healUp();

                    victor = runRound(p1, p2, roundLength);
                    if (victor != null) victories.put(victor.name, victories.get(victor.name) + 1);
                    p1.healUp();
                    p2.healUp();

                    if (victories.get(p1.name) > victories.get(p2.name)) p1.victoriesCount++;
                    if (victories.get(p1.name) < victories.get(p2.name)) p2.victoriesCount++;
                }
            }
        }
    }
}
