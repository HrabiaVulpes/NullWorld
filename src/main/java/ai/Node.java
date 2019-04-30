package ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    public Long ID;
    public Double value;
    Integer layer;
    Double target;
    private Double learningRate = 0.05;
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

//        value = 1 / (1 + Math.pow(Math.E, -value));
//        value = (Math.sqrt(value * value + 1) + 1) / 2 + value;
//        value = value/ (1 + Math.abs(value));
        value = value/ (Math.sqrt(1 + 0.01 * value * value));
        target = value;
    }

    private Double derivativeOfValue(Double value) {
//        return value * (1-value);
//        return value / (2 * Math.sqrt(value * value + 1)) + 1;
//        return 1 / ( Math.pow(1 + Math.abs(value), 2));
        return 1 / ( Math.pow( 1/Math.sqrt(1 + 0.01 * value * value), 3));
    }

    public Double getValue() {
        return (value < 0.0) ? -1.0 : 1.0;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getErrorByNodeId(Long id) {
        return weights.get(id) * (value - target);
    }

    public void recalculateWeights(Map<Long, Double> previousLayerValues, Double sumOfNextLayerErrors) {
        target = value - sumOfNextLayerErrors;
        weights.keySet().forEach(
                key -> weightChange.put(
                        key,
                        sumOfNextLayerErrors * derivativeOfValue(value) * previousLayerValues.get(key)
                )
        );
    }

    public void recalculateWeights(Map<Long, Double> previousLayerValues) {
        weights.keySet().forEach(
                key -> weightChange.put(
                        key,
                        (value - target) * derivativeOfValue(value) * previousLayerValues.get(key)
                )
        );
    }

    public void updateWeights() {
        personalWeight = personalWeight - (((value - target) * derivativeOfValue(value)) * learningRate);
        weights.keySet().forEach(
                key -> weights.put(key, weights.get(key) - (weightChange.get(key) * learningRate))
        );
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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
}
