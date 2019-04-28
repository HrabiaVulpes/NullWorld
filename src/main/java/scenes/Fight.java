package scenes;

import agent.Combatant;

public class Fight extends Training {
    public Fight(Combatant player1, Combatant player2) {
        super(player1, player2);
    }

    @Override
    protected void processTurn() {
        pickMovesStage();
        resolveMovesStage();
        resolveDistanceStage();
        resolveStatesStage();
    }
}
