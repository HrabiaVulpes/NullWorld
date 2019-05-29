package combat_data;

import java.util.Arrays;
import java.util.Collection;

public enum DamageTypes {
    PIERCE,
    SLASH,
    BLUNT,
    NONE;

    public static Collection<DamageTypes> effectiveTypes(){
        return Arrays.asList(PIERCE, SLASH, BLUNT);
    }
}
