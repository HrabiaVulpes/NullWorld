package scenes;

import agent.LearningCombatant;

public class TrainingAI extends DuelBase {
    public TrainingAI(LearningCombatant player1, LearningCombatant player2) {
        super(player1, player2);
    }

    @Override
    protected void pickMovesStage() {
        ((LearningCombatant) player1).setStates(player2.statesList, distance, player1.weapon);
        ((LearningCombatant) player2).setStates(player1.statesList, distance, player2.weapon);

        player1.pickMove();
        player2.pickMove();
    }

    @Override
    protected void processTurn() {
        if (player1.hitPoints <= 0 || player2.hitPoints <= 0) return;

        pickMovesStage();
        resolveMovesStage();

        ((LearningCombatant) player1).learn(p1Effect, player2.damageDealt(p2Effect), distance);
        ((LearningCombatant) player2).learn(p2Effect, player1.damageDealt(p1Effect), distance);

        resolveDistanceStage();
        resolveStatesStage();
    }
}
