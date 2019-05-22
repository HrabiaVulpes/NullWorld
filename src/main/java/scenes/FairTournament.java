package scenes;

import agent.Combatant;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FairTournament extends Tournament {
    Map<String, List<Integer>> finalScores = new HashMap<>();

    public FairTournament(List<Combatant> combatants) {
        this.combatants = combatants;

        combatants.forEach(
                combatant -> finalScores.put(combatant.name, new ArrayList<>())
        );
    }

    public Combatant runRound(Combatant player1, Combatant player2, int roundLength) {
        Training fight = new Training(player1, player2);
        System.out.println(fight);
        fight.fightForRounds(roundLength);
        return fight.winner();
    }

    @Override
    public void runTournament(int rounds, int roundLenght) {
        for (int i = 0; i < rounds; i++) {
            runSingleRoundFights(i, roundLenght);
            printToFile();
            resetWins();
        }
    }

    @Override
    public void eternalTournament(int rounds, int roundLenght) {
        while(true) {
            for (int i = 0; i < rounds; i++) {
                runSingleRoundFights(i, roundLenght);
            }
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

    public void runSingleRoundFights(int currentRound, int roundLenght) {
        for (Combatant p1 : combatants) {
            for (Combatant p2 : combatants) {
                if (!p1.name.equals(p2.name)) {
                    Map<String, Integer> victories = new HashMap<>(Map.of(p1.name, 0, p2.name, 0));
                    Combatant victor = runRound(p1, p2, roundLenght);
                    if (victor != null) victories.put(victor.name, victories.get(victor.name) + 1);
                    p1.healUp();
                    p2.healUp();

                    victor = runRound(p1, p2, roundLenght);
                    if (victor != null) victories.put(victor.name, victories.get(victor.name) + 1);
                    p1.healUp();
                    p2.healUp();

                    victor = runRound(p1, p2, roundLenght);
                    if (victor != null) victories.put(victor.name, victories.get(victor.name) + 1);
                    p1.healUp();
                    p2.healUp();

                    if (victories.get(p1.name) > victories.get(p2.name)) p1.victoriesCount++;
                    if (victories.get(p1.name) < victories.get(p2.name)) p2.victoriesCount++;
                }
            }
        }

        combatants.forEach(
                combatant -> finalScores.get(combatant.name).add(combatant.getVictoriesCount())
        );
    }
}
