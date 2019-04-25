package combat_data;

import java.util.Arrays;
import java.util.List;

public enum States {
    ABOVE,
    TURNED,
    CROUCHED,
    KNOCKED,

    STAGGERED,

    WEAPON_SIDE,
    WEAPON_EXTENDED,
    WEAPON_LOW,
    WEAPON_HIGH;

    public static List<States> getAll(){
        return Arrays.asList(
                ABOVE, TURNED, CROUCHED, KNOCKED, WEAPON_SIDE, WEAPON_EXTENDED, WEAPON_HIGH, WEAPON_LOW, STAGGERED
        );
    }
}
