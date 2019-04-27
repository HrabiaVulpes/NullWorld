package scenes;

import agent.Combatant;
import combat_data.Effect;
import combat_data.MoveTypes;
import combat_data.States;

import java.util.stream.Collectors;

public class Duel {
    private Effect p1Effect;
    private Effect p2Effect;
    private Combatant player1;
    private Combatant player2;
    private Integer distance = 2;
    private Integer maxDistance = 5;

    public Duel(Combatant player1, Combatant player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    Combatant winner() {
        if (player1.hitPoints > player2.hitPoints) return player1;
        if (player2.hitPoints > player1.hitPoints) return player2;
        return null;
    }

    Combatant looser() {
        if (player1.hitPoints > player2.hitPoints) return player2;
        if (player2.hitPoints > player1.hitPoints) return player1;
        return null;
    }

    private void pickMovesStage() {
        if (player1.hitPoints <= 0 || player2.hitPoints <= 0) return;

        player1.setStates(player2.statesList, distance, player1.weapon.getLength());
        player2.setStates(player1.statesList, distance, player2.weapon.getLength());

        player1.pickMove();
        player2.pickMove();
    }

    private void resolveMovesStage() {
        Effect p1Effect = player1.move.resolveAgainst(player2.move, player2.weapon.getLength(), distance);
        Effect p2Effect = player2.move.resolveAgainst(player1.move, player1.weapon.getLength(), distance);

        System.out.println(player1.name + ": " + player1.move.getType().name() + "=" + p1Effect +
                "\t" + player2.name + ": " + player2.move.getType().name() + "=" + p2Effect);

        if (p1Effect == Effect.HIT) player2.hitPoints -= (int) Math.round(player1.damageDealt());
        if (p1Effect == Effect.CRIT) player2.hitPoints -= (int) Math.round(2 * player1.damageDealt());

        if (p2Effect == Effect.HIT) player1.hitPoints -= (int) Math.round(player2.damageDealt());
        if (p2Effect == Effect.CRIT) player1.hitPoints -= (int) Math.round(2 * player2.damageDealt());
    }

    private void resolveDistanceStage() {
        if (player1.move.getType() == MoveTypes.BACK_AWAY && distance < maxDistance)
            distance++;
        if (player2.move.getType() == MoveTypes.BACK_AWAY && distance < maxDistance)
            distance++;

        if (player1.move.getType() == MoveTypes.CLOSE_IN && distance > 0)
            distance--;
        if (player2.move.getType() == MoveTypes.CLOSE_IN && distance > 0)
            distance--;
    }

    private void resolveStatesStage() {
        //basic states from move
        player1.statesList.removeAll(player1.move.getRemovedStates());
        player2.statesList.removeAll(player2.move.getRemovedStates());

        //turned after side attack misses
        if (player1.move.getAddedStates().contains(States.WEAPON_SIDE) && p1Effect == Effect.MISS)
            player1.statesList.add(States.TURNED);
        if (player2.move.getAddedStates().contains(States.WEAPON_SIDE) && p2Effect == Effect.MISS)
            player2.statesList.add(States.TURNED);

        //remove above - gravity
        player1.statesList.remove(States.ABOVE);
        player2.statesList.remove(States.ABOVE);

        //add basic states
        player1.statesList.addAll(player1.move.getAddedStates());
        player2.statesList.addAll(player2.move.getAddedStates());

        //if one of players used CLOSE_IN and parried, add weapon states of the other
        if (player1.move.getType() == MoveTypes.CLOSE_IN && p1Effect == Effect.PARRY)
            player1.statesList.addAll(player2.move.getAddedStates()
                    .stream()
                    .filter(state -> state.name().contains("WEAPON"))
                    .collect(Collectors.toList())
            );

        if (player2.move.getType() == MoveTypes.CLOSE_IN && p2Effect == Effect.PARRY)
            player2.statesList.addAll(player1.move.getAddedStates()
                    .stream()
                    .filter(state -> state.name().contains("WEAPON"))
                    .collect(Collectors.toList())
            );

        //if one of players got crit, he is staggered
        if (p1Effect == Effect.CRIT) player2.statesList.add(States.STAGGERED);
        if (p2Effect == Effect.CRIT) player1.statesList.add(States.STAGGERED);
    }

    private void processTurn() {
        pickMovesStage();
        resolveMovesStage();

        player1.learn(p1Effect, p2Effect, distance);
        player2.learn(p2Effect, p1Effect, distance);

        resolveDistanceStage();
        resolveStatesStage();
    }

    Combatant fightForRounds(int rounds) {
        for (int i = 0; i < rounds; i++) {
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
