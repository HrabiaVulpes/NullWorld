import ai.NeuralNetwork;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import combat_data.Weapon;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    static Double[][] learnings = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0}
    };

    private static Double[] pickRandomLearning() {
        int learning = Math.toIntExact(Math.round(Math.random() * 1000)) % 4;
        return learnings[learning];
    }

    private static Boolean sufficienSuccessRatio(NeuralNetwork xorNetwork){
        int successes = 0;

        for (Double[] learn : learnings){
            Map<Long, Double> input = Map.of(0L, learn[0], 1L, learn[1]);
            Double output = learn[2];

            xorNetwork.setValues(input);
            xorNetwork.calculateFullPass();
            Double reality = xorNetwork.getNodeById(4L).getValue();
            if (reality.equals(output)) successes++;

        }
        return successes == 3;
    }

    public static void main(String[] args) {
        NeuralNetwork xorNetwork = new NeuralNetwork(2, 2, 1);
        Integer learningPasses = 0;
        while(!sufficienSuccessRatio(xorNetwork)){
            Double[] learn = pickRandomLearning();
            Map<Long, Double> input = Map.of(0L, learn[0], 1L, learn[1]);
            Map<Long, Double> output = Map.of(4L, learn[2]);

            xorNetwork.setValues(input);
            xorNetwork.calculateFullPass();
//            BigDecimal reality = BigDecimal.valueOf(xorNetwork.getNodeById(4L).value).setScale(2, RoundingMode.HALF_UP);
//            System.out.println("X1 = " + learn[0] + "\t X2 = " + learn[1] + "\t Y = " + learn[2] + "\t Y1 = " + reality + "\t Y2 = " + xorNetwork.getNodeById(4L).getValue());
            System.out.println("Pass number: " + learningPasses++);
            xorNetwork.expectValues(output);
            xorNetwork.recalculateFullPass();
            xorNetwork.updateWeights();
        }
    }

    private static void jsonLoading() {
        List<Weapon> weaponList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/dagger.json"), weaponList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = Main.class.getClassLoader().getResource("weapons.json").getFile();

        System.out.println(filePath);

        List<Weapon> shivs = null;
        try {
            shivs = objectMapper.readValue(new File(filePath), new TypeReference<List<Weapon>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert shivs != null;
        shivs.forEach(weapon -> System.out.println(weapon.getName()));
    }

}