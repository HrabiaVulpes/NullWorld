package combat_data;

import java.util.Arrays;
import java.util.List;

public enum MoveTypes {
    OVERHEAD(0L),
    SLASH(1L),
    KICK(2L),

    SHOT(3L),
    THRUST(4L),
    UNDERSTRIKE(5L),

    JUMP(6L),
    DODGE(7L),
    DUCK(8L),

    CLOSE_IN(9L),
    BACK_AWAY(10L),
    GET_UP(11L),

    SPECIAL1(12L),
    SPECIAL2(13L),
    SPECIAL3(14L);

    Long id;

    MoveTypes(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static List<MoveTypes> getAll(){
        return Arrays.asList(
                OVERHEAD,
                SLASH,
                KICK,
                SHOT,
                THRUST,
                UNDERSTRIKE,
                JUMP,
                DODGE,
                DUCK,
                CLOSE_IN,
                BACK_AWAY,
                GET_UP,
                SPECIAL1,
                SPECIAL2,
                SPECIAL3
        );
    }
}
