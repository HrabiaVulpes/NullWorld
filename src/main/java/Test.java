import ai.Mind;
import ai.MindFuck;

public class Test {
    static Mind mind = new Mind().registerKnowledge("input1")
            .registerKnowledge("input2")
            .registerDecision("output")
            .buildBrain(1);

    static Double[][] and = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {1.0, 0.0, 0.0},
            {1.0, 1.0, 1.0}
    };

    static Double[][] or = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {1.0, 1.0, 1.0}
    };

    static Double[][] xor = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0}
    };

    static void teach(Double[][] data) throws MindFuck {
        for (int i = 0; i < 4; i++) {
            mind
                    .letItKnow("input1", data[i][0])
                    .letItKnow("input2", data[i][1])
                    .nowThink()
                    .resultIs("output", data[i][2])
                    .rethinkIt();
        }
    }

    static void print(Double[][] data) throws MindFuck {
        for (int i = 0; i < 4; i++) {
            Double result = mind.letItKnow("input1", data[i][0])
                    .letItKnow("input2", data[i][1])
                    .nowThink()
                    .askWhether("output");
            System.out.println("Test: {" + data[i][0] + ", " + data[i][1] + "} -> " + result);
        }
    }

    static boolean test(Double[][] data) throws MindFuck {
        for (int i = 0; i < 4; i++) {
            Double result = mind.letItKnow("input1", data[i][0])
                    .letItKnow("input2", data[i][1])
                    .nowThink()
                    .askWhether("output");
            if (!result.equals(data[i][2])) return false;
        }
        return true;
    }

    static int howLongTillPass(int max) throws MindFuck {
        for (int i = 0; i< max; i++){
            teach(xor);
            if (test(xor)) return i;
        }
        return max;
    }

    public static void main(String[] args) {
        try {
            System.out.println(howLongTillPass(10000));
        } catch (MindFuck mindFuck) {
            mindFuck.printStackTrace();
        }
    }
}
