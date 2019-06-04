package scenes.duels;

import agent.Player;

import static userInterface.ConsoleUtils.*;

public class HumanVsAi extends DuelBase {

    public HumanVsAi(Player player1, Player player2) {
        super(player1, player2);
    }

    @Override
    protected void pickMovesStage() {

        //set value for node in neural network
        player2.setStates(player1.statesList, distance, player2.weapon);

        player1.pickMove();
        player2.pickMove();
    }

    @Override
    protected boolean processTurn() {
        if (player1.hitPoints <= 0 || player2.hitPoints <= 0) return false;
        showStatus(player2);
        showDistance(distance);
        showStatus(player1);
        availableMoves(player1);

        pickMovesStage();
        resolveMovesStage();

        player2.learn(player2.move, p2Effect, player1.move, p1Effect, distance);

        resolveDistanceStage();
        resolveStatesStage();
        return true;
    }
}

