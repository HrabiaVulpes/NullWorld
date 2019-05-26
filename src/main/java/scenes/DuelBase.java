package scenes;

import agent.Combatant;
import combat_data.Effect;

public class DuelBase {
    protected Combatant player1;
    protected Combatant player2;
    private Effect p1Effect;
    private Effect p2Effect;

    public DuelBase(Combatant player1, Combatant player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    Combatant winner() {
        if (player1.hitPoints > player2.hitPoints) return player1;
        if (player2.hitPoints > player1.hitPoints) return player2;
        return null;
    }

    Combatant loser() {
        if (player1.hitPoints > player2.hitPoints) return player2;
        if (player2.hitPoints > player1.hitPoints) return player1;
        return null;
    }


}
