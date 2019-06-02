package combat_data;

import java.util.*;

public class Move {
    private MoveTypes type;
    private DamageTypes damageType;

    private List<States> unavailableOn = Collections.emptyList();

    private Set<PositionTags> positionDuringMove = Collections.emptySet();
    private Set<PositionTags> weaponMovement = Collections.emptySet();

    private Set<States> removedStates = Collections.emptySet();
    private Set<States> addedStates = Collections.emptySet();

    public Move() {
    }

    public Move(MoveTypes type) {
        this.type = type;
    }

    public Move withDamage(DamageTypes damage) {
        this.damageType = damage;
        return this;
    }

    public Effect resolveAgainst(Move other, Collection<States> myStates, Integer weaponLength, Integer distanceToEnemy) {
        if (type == MoveTypes.BLOCK && other.damageType != DamageTypes.NONE) return Effect.PARRY;
        if (damageType == DamageTypes.NONE) return Effect.MISS;

        if (type == MoveTypes.KICK && distanceToEnemy > 1) return Effect.MISS;
        if (type != MoveTypes.SHOT && distanceToEnemy > weaponLength) return Effect.MISS;

        ArrayList<PositionTags> commonWeaponMovements = new ArrayList<>(this.getWeaponMovement());
        commonWeaponMovements.retainAll(other.getWeaponMovement());
        commonWeaponMovements.remove(PositionTags.STANDARD);

        if (!commonWeaponMovements.isEmpty()) return Effect.PARRY;

        ArrayList<PositionTags> weaponVsBody = new ArrayList<>(this.getWeaponMovement());
        weaponVsBody.retainAll(other.getPositionDuringMove());
        ArrayList<PositionTags> weaponVsBodyNoStandard = new ArrayList<>(weaponVsBody);
        weaponVsBodyNoStandard.remove(PositionTags.STANDARD);

        if (!weaponVsBodyNoStandard.isEmpty()) return Effect.CRIT;
        if (!weaponVsBody.isEmpty()) {
            if (type == MoveTypes.OVERHEAD && myStates.contains(States.ABOVE)) return Effect.CRIT;
            if (type == MoveTypes.UNDERSTRIKE && myStates.contains(States.CROUCHED)) return Effect.CRIT;
            return Effect.HIT;
        }

        return Effect.MISS;
    }

    public Move withUnavailableOn(States... unavailableOn) {
        this.unavailableOn = Arrays.asList(unavailableOn);
        return this;
    }

    public Move withPositionDuringMove(PositionTags... positionDuringMove) {
        this.positionDuringMove = new HashSet<>(Arrays.asList(positionDuringMove));
        return this;
    }

    public Move withWeaponMovement(PositionTags... weaponMovement) {
        this.weaponMovement = new HashSet<>(Arrays.asList(weaponMovement));
        return this;
    }

    public Move withRemovedStates(States... removedStates) {
        this.removedStates = new HashSet<>(Arrays.asList(removedStates));
        return this;
    }

    public Move withRemovedStates(List<States> removedStates) {
        this.removedStates = new HashSet<>(removedStates);
        return this;
    }

    public Move withAddedStates(States... addedStates) {
        this.addedStates = new HashSet<>(Arrays.asList(addedStates));
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

    public Set<PositionTags> getPositionDuringMove() {
        return positionDuringMove;
    }

    public void setPositionDuringMove(List<PositionTags> positionDuringMove) {
        this.positionDuringMove = new HashSet<>(positionDuringMove);
    }

    public Set<PositionTags> getWeaponMovement() {
        return weaponMovement;
    }

    public void setWeaponMovement(List<PositionTags> weaponMovement) {
        this.weaponMovement = new HashSet<>(weaponMovement);
    }

    public Set<States> getRemovedStates() {
        return removedStates;
    }

    public void setRemovedStates(List<States> removedStates) {
        this.removedStates = new HashSet<>(removedStates);
    }

    public Set<States> getAddedStates() {
        return addedStates;
    }

    public void setAddedStates(List<States> addedStates) {
        this.addedStates = new HashSet<>(addedStates);
    }
}
