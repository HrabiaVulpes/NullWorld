package agent.simpleAIAgent;

import agent.Player;
import combat_data.Move;
import combat_data.MoveTypes;
import combat_data.States;
import combat_data.Weapon;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ForeverBest extends Player {
    private Integer distanceToEnemy;

    public ForeverBest(String name, Weapon weapon) {
        super(name, weapon);
    }

    @Override
    public void setStates(Collection<States> enemyStates, Integer enemyDistance, Weapon enemyWeapon) {
        this.distanceToEnemy = enemyDistance;
    }

    @Override
    public void pickMove() {
        if (distanceToEnemy > weapon.getLength()) {
            move = weapon.getOptionByType(MoveTypes.CLOSE_IN);
            return;
        }

        Set<MoveTypes> optionTypes = getAvailableMoves();
        List<Move> options = optionTypes.stream()
                .map(type -> weapon.getOptionByType(type))
                .collect(Collectors.toList());

        Move best = weapon.getOptionByType(MoveTypes.BACK_AWAY);

        for (Move current : options) {
            if (getDamageByType(current.getDamageType()) > getDamageByType(best.getDamageType()))
                best = current;
        }

        move = best;
    }
}
