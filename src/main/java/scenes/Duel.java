package scenes;

import agent.Combatant;
import combat_data.Effect;
import combat_data.MoveTypes;

public class Duel {
    private Combatant player1;
    private Combatant player2;
    private Integer distance = 2;
    private Integer maxDistance = 5;


    public Duel(Combatant player1, Combatant player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void processTurn() {
        player1.setStates(player2.statesList, Math.abs(distance));
        player2.setStates(player1.statesList, Math.abs(distance));

        player1.pickMove();
        player2.pickMove();

        Effect p1Effect = player1.move.resolveAgainst(player2.move, player2.weapon.getLength(), distance);
        Effect p2Effect = player2.move.resolveAgainst(player1.move, player1.weapon.getLength(), distance);

        player1.learn(p1Effect, p2Effect, distance);
        player2.learn(p2Effect, p1Effect, distance);

        if (player1.move.getType() == MoveTypes.BACK_AWAY && p2Effect != Effect.HIT && p2Effect != Effect.CRIT && distance < maxDistance)
            distance++;
        if (player2.move.getType() == MoveTypes.BACK_AWAY && p1Effect != Effect.HIT && p2Effect != Effect.CRIT && distance < maxDistance)
            distance++;

        if (player1.move.getType() == MoveTypes.CLOSE_IN && p2Effect != Effect.HIT && p2Effect != Effect.CRIT && distance > 0)
            distance--;
        if (player2.move.getType() == MoveTypes.CLOSE_IN && p1Effect != Effect.HIT && p2Effect != Effect.CRIT && distance > 0)
            distance--;
    }
}
