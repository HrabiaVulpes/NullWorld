package ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private Double learningRate = 0.3;
    public Long ID;
    public Double value;
    Integer layer;
    Double target;

    private Map<Long, Double> weights;
    private Map<Long, Double> weightChange;
    private Double personalWeight;

    public Node() {
    }

    public Node(Long nodeId, Integer layer, List<Long> previousLayer) {
        this.layer = layer;
        this.ID = nodeId;
        this.value = Math.random();
        this.target = 0.0;
        this.weights = new HashMap<>();
        this.weightChange = new HashMap<>();
        this.personalWeight = Math.random();
        previousLayer.forEach(id -> this.weights.put(id, Math.random()));
    }

    public void calculateValue(Map<Long, Double> previousLayer) {
        value = personalWeight;
        weights.keySet().forEach(
                key -> value += previousLayer.get(key) * weights.get(key)
        );

        value = 1 / (1 + Math.pow(Math.E, -value));
        target = value;
    }

    public Double getValue() {
        return (value < 0.0) ? 0.0 : 1.0;
    }

    public Double getErrorByNodeId(Long id) {
        return weights.get(id) * (value - target);
    }

    public void recalculateWeights(Map<Long, Double> previousLayerValues, Double sumOfNextLayerErrors) {
        target = value - sumOfNextLayerErrors;
        weights.keySet().forEach(
                key -> weightChange.put(
                        key,
                        sumOfNextLayerErrors * value * (1 - value) * previousLayerValues.get(key)
                )
        );
    }

    public void recalculateWeights(Map<Long, Double> previousLayerValues) {
        weights.keySet().forEach(
                key -> weightChange.put(
                        key,
                        (value - target) * value * (1 - value) * previousLayerValues.get(key)
                )
        );
    }

    public void updateWeights() {
        personalWeight = personalWeight - (((value - target) * value * (1 - value)) * learningRate);
        weights.keySet().forEach(
                key -> weights.put(key, weights.get(key) - (weightChange.get(key) * learningRate))
        );
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Map<Long, Double> getWeights() {
        return weights;
    }

    public void setWeights(Map<Long, Double> weights) {
        this.weights = weights;
    }

    public Map<Long, Double> getWeightChange() {
        return weightChange;
    }

    public void setWeightChange(Map<Long, Double> weightChange) {
        this.weightChange = weightChange;
    }

    public Double getPersonalWeight() {
        return personalWeight;
    }

    public void setPersonalWeight(Double personalWeight) {
        this.personalWeight = personalWeight;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }
}
