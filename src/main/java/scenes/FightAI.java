package scenes;

import agent.Combatant;

public class FightAI extends DuelBase {
    public FightAI(Combatant player1, Combatant player2) {
        super(player1, player2);
    }

    @Override
    protected void pickMovesStage() {
        ((Combatant) player1).setStates(player2.statesList, distance, player1.weapon);
        ((Combatant) player2).setStates(player1.statesList, distance, player2.weapon);

        player1.pickMove();
        player2.pickMove();
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
