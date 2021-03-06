package scenes.duels;

import agent.Player;

public class FightAI extends DuelBase {
    public FightAI(Player player1, Player player2) {
        super(player1, player2);
    }

    @Override
    protected void pickMovesStage() {
        player1.setStates(player2.statesList, distance, player1.weapon);
        player2.setStates(player1.statesList, distance, player2.weapon);

        player1.pickMove();
        player2.pickMove();
    }

    @Override
    protected boolean processTurn() {
        if (player1.hitPoints <= 0 || player2.hitPoints <= 0) return false;

        pickMovesStage();
        resolveMovesStage();
        resolveDistanceStage();
        resolveStatesStage();
        return true;
    }
}
