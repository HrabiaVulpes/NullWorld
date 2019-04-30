package ai;

import org.junit.jupiter.api.Test;

import java.util.Map;

class NodeTest {
    private Double[][] xor = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {0.0, 1.0, 0.0}
    };

    public boolean checkXORNetwork(NeuralNetwork neuralNetwork) {
        int successes = 0;

        for (Double[] set : xor) {
            neuralNetwork.setValues(Map.of(0L, set[0], 1L, set[1]));
            neuralNetwork.calculateFullPass();
            if (neuralNetwork.getNodeById(4L).getValue().equals(set[2])) successes++;
        }
        return successes == xor.length;
    }

    @Test
    void nodeTest() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 2, 1);
        int time = 0;

        for (int i = 0; i < 1000; i++, time++) {
            for (Double[] set : xor) {
                neuralNetwork.setValues(Map.of(0L, set[0], 1L, set[1]));
                neuralNetwork.calculateFullPass();
                neuralNetwork.expectValues(Map.of(4L, set[2]));
            }

            if (checkXORNetwork(neuralNetwork)) break;
        }
        System.out.println("Time needed to learn = " + time);
    }

}