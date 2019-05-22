package scenes;

import agent.Combatant;

public class Fight extends Training {
    public Fight(Combatant player1, Combatant player2) {
        super(player1, player2);
    }

    @Override
    protected void processTurn() {
        if (player1.hitPoints <= 0 || player2.hitPoints <= 0) return;

        pickMovesStage();
        resolveMovesStage();
        resolveDistanceStage();
        resolveStatesStage();
    }
}
