package ai;

import java.util.*;
import java.util.stream.Collectors;

public class NeuralNetwork {
    private Long lastNodeId = 0L;
    private List<Node> nodes;
    private Integer amountOfLayers = 0;

    public NeuralNetwork(){}

    public NeuralNetwork(Integer... layers) {
        lastNodeId = 0L;
        nodes = new ArrayList<>();
        for (Integer layer : layers) {
            for (int i = 0; i < layer; i++) {
                Integer localLayerNumber = amountOfLayers;
                nodes.add(new Node(
                                lastNodeId++,
                                amountOfLayers,
                                nodes.stream()
                                        .filter(node -> node.layer.equals(localLayerNumber - 1))
                                        .map(node -> node.ID)
                                        .collect(Collectors.toList())
                        )
                );
            }
            amountOfLayers++;
        }
    }

    public Node getNodeById(Long nodeId) {
        return nodes.stream()
                .filter(node -> node.ID.equals(nodeId))
                .findFirst()
                .orElse(new Node(-1L, -1, Collections.emptyList()));
    }

    private List<Node> getNodesByLayer(Integer layer) {
        return nodes.stream()
                .filter(node -> node.layer.equals(layer))
                .collect(Collectors.toList());
    }

    public void setValues(Map<Long, Double> values) {
        values.keySet().forEach(key -> getNodeById(key).value = values.get(key));
    }

    private void calculateLayer(Integer layerNumber) {
        Map<Long, Double> previousLayer = new HashMap<>();
        getNodesByLayer(layerNumber - 1).forEach(node -> previousLayer.put(node.ID, node.value));
        getNodesByLayer(layerNumber).forEach(node -> node.calculateValue(previousLayer));
    }

    public void calculateFullPass() {
        for (int i = 1; i < amountOfLayers; i++) {
            calculateLayer(i);
        }
    }

    public void expectValues(Map<Long, Double> values) {
        values.keySet().forEach(key -> getNodeById(key).target = values.get(key));
    }

    private void recalculateLayerError(Integer layerNumber) {
        if (layerNumber > 0 && layerNumber < amountOfLayers - 1) {
            Map<Long, Double> previousLayer = new HashMap<>();
            getNodesByLayer(layerNumber - 1).forEach(node -> previousLayer.put(node.ID, node.value));
            getNodesByLayer(layerNumber).forEach(node -> node.recalculateWeights(
                    previousLayer,
                    getNodesByLayer(layerNumber + 1).stream()
                            .map(n -> n.getErrorByNodeId(node.ID))
                            .mapToDouble(i -> i)
                            .sum()
                    )
            );
        } else if (layerNumber == amountOfLayers - 1) {
            Map<Long, Double> previousLayer = new HashMap<>();
            getNodesByLayer(layerNumber - 1).forEach(node -> previousLayer.put(node.ID, node.value));
            getNodesByLayer(layerNumber).forEach(node -> node.recalculateWeights(previousLayer));
        }

    }

    public void recalculateFullPass() {
        for (int i = amountOfLayers - 1; i >= 0; i--) {
            recalculateLayerError(i);
        }
    }

    public void updateWeights() {
        nodes.forEach(Node::updateWeights);
    }

    public Long getLastNodeId() {
        return lastNodeId;
    }

    public static void setLastNodeId(Long lastNodeId) {
        lastNodeId = lastNodeId;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public Integer getAmountOfLayers() {
        return amountOfLayers;
    }

    public void setAmountOfLayers(Integer amountOfLayers) {
        this.amountOfLayers = amountOfLayers;
    }
}
