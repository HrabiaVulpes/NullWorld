package ai;

import org.junit.jupiter.api.Test;

import java.util.Map;

class NodeTest {
    private Double[][] xor = {
            {0.0, 0.0, -1.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {0.0, 0.0, -1.0}
    };

    public int checkXORNetwork(NeuralNetwork neuralNetwork) {
        int successes = 0;

        for (Double[] set : xor) {
            neuralNetwork.setValues(Map.of(0L, set[0], 1L, set[1]));
            neuralNetwork.calculateFullPass();
            if (neuralNetwork.getNodeById(4L).getValue().equals(set[2])) successes++;
        }
        return successes;
    }

    @Test
    void nodeTest() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 2, 1);
        int time = 0;

        for (int i = 0; i < 30000; i++, time++) {
            int rando = (int) (Math.random() * 100 % 4);
            Double[] set = xor[rando];
            neuralNetwork.setValues(Map.of(0L, set[0], 1L, set[1]));
            neuralNetwork.calculateFullPass();
            neuralNetwork.expectValues(Map.of(4L, set[2]));
            neuralNetwork.recalculateFullPass();
            neuralNetwork.updateWeights();

            if (checkXORNetwork(neuralNetwork) == 4) break;
        }

        System.out.println("Time needed to learn= " + time + " result= " + checkXORNetwork(neuralNetwork));
        for (Double[] set : xor) {
            neuralNetwork.setValues(Map.of(0L, set[0], 1L, set[1]));
            neuralNetwork.calculateFullPass();
            System.out.println(set[0] + " " + set[1] + " " + set[2] + " > " + neuralNetwork.getNodeById(4L).value);
        }

        assert checkXORNetwork(neuralNetwork) == 4;
    }

}