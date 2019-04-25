package combat_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Move {
    private MoveTypes type;
    private DamageTypes damageType;

    private List<States> unavailableOn = Collections.emptyList();

    private List<PositionTags> positionDuringMove = Collections.emptyList();
    private List<PositionTags> weaponMovement = Collections.emptyList();

    private List<States> removedStates = Collections.emptyList();
    private List<States> addedStates = Collections.emptyList();

    public Move() {
    }

    public Move(MoveTypes type) {
        this.type = type;
    }

    public Move withDamage(DamageTypes damage) {
        this.damageType = damage;
        return this;
    }

    public Effect resolveAgainst(Move other, Integer weaponLength, Integer distanceToEnemy) {
        if (weaponLength < distanceToEnemy || damageType == DamageTypes.NONE) return  Effect.MISS;

        ArrayList<PositionTags> commonWeaponMovements = new ArrayList<>(this.getWeaponMovement());
        commonWeaponMovements.retainAll(other.getWeaponMovement());

        if (commonWeaponMovements.size() >= 2)
            return Effect.PARRY;

        ArrayList<PositionTags> weaponVsBody = new ArrayList<>(this.getWeaponMovement());
        weaponVsBody.retainAll(other.getPositionDuringMove());

        if (weaponVsBody.size() > 1) return Effect.CRIT;
        if (weaponVsBody.size() == 1) return Effect.HIT;

        return Effect.MISS;
    }

    public Move withUnavailableOn(States... unavailableOn) {
        this.unavailableOn = Arrays.asList(unavailableOn);
        return this;
    }

    public Move withPositionDuringMove(PositionTags... positionDuringMove) {
        this.positionDuringMove = Arrays.asList(positionDuringMove);
        return this;
    }

    public Move withWeaponMovement(PositionTags... weaponMovement) {
        this.weaponMovement = Arrays.asList(weaponMovement);
        return this;
    }

    public Move withRemovedStates(States... removedStates) {
        this.removedStates = Arrays.asList(removedStates);
        return this;
    }

    public Move withRemovedStates(List<States> removedStates) {
        this.removedStates = removedStates;
        return this;
    }

    public Move withAddedStates(States... addedStates) {
        this.addedStates = Arrays.asList(addedStates);
        return this;
    }

    public MoveTypes getType() {
        return type;
    }

    public void setType(MoveTypes type) {
        this.type = type;
    }

    public DamageTypes getDamageType() {
        return damageType;
    }

    public void setDamageType(DamageTypes damageType) {
        this.damageType = damageType;
    }

    public List<States> getUnavailableOn() {
        return unavailableOn;
    }

    public void setUnavailableOn(List<States> unavailableOn) {
        this.unavailableOn = unavailableOn;
    }

    public List<PositionTags> getPositionDuringMove() {
        return positionDuringMove;
    }

    public void setPositionDuringMove(List<PositionTags> positionDuringMove) {
        this.positionDuringMove = positionDuringMove;
    }

    public List<PositionTags> getWeaponMovement() {
        return weaponMovement;
    }

    public void setWeaponMovement(List<PositionTags> weaponMovement) {
        this.weaponMovement = weaponMovement;
    }

    public List<States> getRemovedStates() {
        return removedStates;
    }

    public void setRemovedStates(List<States> removedStates) {
        this.removedStates = removedStates;
    }

    public List<States> getAddedStates() {
        return addedStates;
    }

    public void setAddedStates(List<States> addedStates) {
        this.addedStates = addedStates;
    }
}
