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

    public Combatant winner(){
        if (player1.hitPoints > player2.hitPoints) return player1;
        if (player2.hitPoints > player1.hitPoints) return player2;
        return null;
    }

    public Combatant looser(){
        if (player1.hitPoints > player2.hitPoints) return player2;
        if (player2.hitPoints > player1.hitPoints) return player1;
        return null;
    }

    public void processTurn() {
        if (player1.hitPoints <= 0 || player2.hitPoints <= 0) return;

        player1.setStates(player2.statesList, distance);
        player2.setStates(player1.statesList, distance);

        player1.pickMove();
        player2.pickMove();

        Effect p1Effect = player1.move.resolveAgainst(player2.move, player2.weapon.getLength(), distance);
        Effect p2Effect = player2.move.resolveAgainst(player1.move, player1.weapon.getLength(), distance);

        System.out.println(player1.name + ": " + player1.move.getType().name() + "=" + p1Effect +
                "\t" + player2.name + ": " + player2.move.getType().name() + "=" + p2Effect);

        if (p1Effect == Effect.HIT) player2.hitPoints -= (int) Math.round(player1.damageDealt());
        if (p1Effect == Effect.CRIT) player2.hitPoints -= (int) Math.round(2*player1.damageDealt());

        if (p2Effect == Effect.HIT) player1.hitPoints -= (int) Math.round(player2.damageDealt());;
        if (p2Effect == Effect.CRIT) player1.hitPoints -= (int) Math.round(2*player2.damageDealt());


        player1.learn(p1Effect, p2Effect, distance);
        player2.learn(p2Effect, p1Effect, distance);

        if (player1.move.getType() == MoveTypes.BACK_AWAY && p2Effect != Effect.HIT && p2Effect != Effect.CRIT && distance < maxDistance)
            distance++;
        if (player2.move.getType() == MoveTypes.BACK_AWAY && p1Effect != Effect.HIT && p2Effect != Effect.CRIT && distance < maxDistance)
            distance++;

        if (player1.move.getType() == MoveTypes.KICK && distance < maxDistance)
            distance++;
        if (player2.move.getType() == MoveTypes.KICK && distance < maxDistance)
            distance++;

        if (player1.move.getType() == MoveTypes.CLOSE_IN && p2Effect != Effect.HIT && p2Effect != Effect.CRIT && distance > 0)
            distance--;
        if (player2.move.getType() == MoveTypes.CLOSE_IN && p1Effect != Effect.HIT && p2Effect != Effect.CRIT && distance > 0)
            distance--;

        player1.statesList.removeAll(player1.move.getRemovedStates());
        player1.statesList.addAll(player1.move.getAddedStates());

        player2.statesList.removeAll(player2.move.getRemovedStates());
        player2.statesList.addAll(player2.move.getAddedStates());
    }

    public Combatant fightForRounds(int rounds){
        for (int i = 0; i < rounds; i++){
            processTurn();
        }
        return winner();
    }

    @Override
    public String toString() {
        return "In this grand fight we have " + player1.name + " against " + player2.name + "!\n"
                + player1.name + " wields it's trusty " + player1.weapon.getName() + ".\n"
                + player2.name + " wields it's trusty " + player2.weapon.getName() + ".\n";
    }
}
