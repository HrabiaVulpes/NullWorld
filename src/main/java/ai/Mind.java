package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Mind letItKnow(String factName, Double value) throws MindFuck {
        if (knowledge.containsKey(factName)) {
            brain.setValues(Map.of(knowledge.get(factName), value));
        } else throw new MindFuck("This brain does not know what " + factName + " is");
        return this;
    }

    public Mind registerDecision(String factName) {
        if (decisions.size() == 0) decisions.put(factName, 0L);
        decisions.put(factName, Collections.max(decisions.values()) + 1);
        return this;
    }

    public Mind askItIf(String factName, Double value) throws MindFuck {
        if (decisions.containsKey(factName)) {
            brain.setValues(Map.of(decisions.get(factName), value));
        } else throw new MindFuck("This brain does not know what " + factName + " is");
        return this;
    }

    public Mind buildBrain(Double learningRate) {
        Integer middleLayer = (int) (Math.max(knowledge.size(), decisions.size()) * 1.1);
        brain = new NeuralNetwork(knowledge.size(), middleLayer, decisions.size());
        decisions.keySet().forEach(key -> decisions.put(key, decisions.get(key) + middleLayer + knowledge.size()));
        return this;
    }

    public Mind nowThink() {
        brain.calculateFullPass();
        return this;
    }

    public String getBestDecision(String defaultDecision) {
        String best = defaultDecision;
        Double bestValue = -1.0;
        for (String decision : decisions.keySet()) {
            if (brain.getNodeById(decisions.get(decision)).value > bestValue) {
                best = decision;
                bestValue = brain.getNodeById(decisions.get(decision)).value;
            }
        }
        return best;
    }

    public String getWeightedDecision(String defaultDecision) {
        Map<Double, String> ideas = new HashMap<>();
        final Double[] total = {0.0};

        decisions.keySet().forEach(
                decision -> {
                    ideas.put(
                            brain.getNodeById(decisions.get(decision)).value + total[0],
                            decision
                    );
                    total[0] += brain.getNodeById(decisions.get(decision)).value;
                }
        );

        Double random = Math.random() * total[0];
        String best = defaultDecision;
        for (Double value : ideas.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList())) {
            if (random <= value) best = ideas.get(value);
            else break;
        }
        return best;
    }

    public Mind resultIs(String decision, Double result) throws MindFuck {
        if (decisions.containsKey(decision)) {
            brain.expectValues(Map.of(decisions.get(decision), result));
        } else throw new MindFuck("This brain does not know what " + decision + " is");
        return this;
    }

    public Mind rethinkIt(){
        brain.recalculateFullPass();
        brain.updateWeights();
        return this;
    }

    public NeuralNetwork getBrain() {
        return brain;
    }

    public void setBrain(NeuralNetwork brain) {
        this.brain = brain;
    }

    public Map<String, Long> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Map<String, Long> knowledge) {
        this.knowledge = knowledge;
    }

    public Map<String, Long> getDecisions() {
        return decisions;
    }

    public void setDecisions(Map<String, Long> decisions) {
        this.decisions = decisions;
    }
}
