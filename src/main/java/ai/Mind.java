package ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Mind {
    private NeuralNetwork brain;
    private Map<String, Long> knowledge;
    private Map<String, Long> decisions;

    public Mind() {
        knowledge = new HashMap<>();
        decisions = new HashMap<>();
    }

    public Mind registerKnowledge(String factName) {
        if (knowledge.size() == 0) knowledge.put(factName, 0L);
        else knowledge.put(factName, Collections.max(knowledge.values()) + 1);
        return this;
    }

    public Mind registerDecision(String factName) {
        if (decisions.size() == 0) decisions.put(factName, 0L);
        decisions.put(factName, Collections.max(decisions.values()) + 1);
        return this;
    }

    public Mind letItKnow(String factName, Double value) throws MindFuck {
        if (knowledge.containsKey(factName)) {
            brain.setValues(Map.of(knowledge.get(factName), value));
        } else throw new MindFuck("This brain does not know what " + factName + " is");
        return this;
    }

    public Double askWhether(String factName) throws MindFuck {
        if (decisions.containsKey(factName)) {
            return brain.getNodeById(decisions.get(factName)).getValue();
        } else throw new MindFuck("This brain does not know what " + factName + " is");
    }

    public Mind buildBrain(Double learningRate) {
        Integer middleLayer = (int) (Math.max(knowledge.size(), decisions.size()) * 1.1);
        brain = new NeuralNetwork(knowledge.size(), middleLayer, decisions.size());
        decisions.keySet().forEach(key -> decisions.put(key, decisions.get(key) + middleLayer + knowledge.size()));
        brain.setlearningRate(learningRate);
        return this;
    }

    public Mind buildBrain(Integer hiddenLayerCount) {
        Integer middleLayer = (int) (Math.max(knowledge.size(), decisions.size()) * 1.1);
        Integer[] data = new Integer[hiddenLayerCount + 2];

        data[0] = knowledge.size();
        for (int i = 1; i < hiddenLayerCount + 1; i++) data[i] = middleLayer;
        data[hiddenLayerCount + 1] = decisions.size();

        brain = new NeuralNetwork(data);

        Integer decisionStartNode = Arrays.stream(data).mapToInt(a -> a).sum() - decisions.size()-1;
        decisions.keySet().forEach(key -> decisions.put(key, decisions.get(key) + decisionStartNode));
        return this;
    }

    public Mind nowThink() {
        brain.calculateFullPass();
        return this;
    }

    public Mind resultIs(String decision, Double result) throws MindFuck {
        if (decisions.containsKey(decision)) {
            brain.expectValues(Map.of(decisions.get(decision), result));
        } else throw new MindFuck("This brain does not know what " + decision + " is");
        return this;
    }

    public Mind rethinkIt() {
        brain.recalculateFullPass();
        brain.updateWeights();
        return this;
    }
}
