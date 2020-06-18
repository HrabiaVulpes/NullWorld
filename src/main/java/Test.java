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

    static void test(Double[][] data) throws MindFuck {
        for (int i = 0; i < 4; i++) {
            Double result = mind.letItKnow("input1", data[i][0])
                    .letItKnow("input2", data[i][1])
                    .nowThink()
                    .askWhether("output");
            System.out.println("Test: {" + data[i][0] + ", " + data[i][1] + "} -> " + result);
        }
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10000; i++) teach(xor);
            test(xor);
        } catch (MindFuck mindFuck) {
            mindFuck.printStackTrace();
        }
    }
}
