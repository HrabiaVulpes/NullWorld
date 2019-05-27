package combat_data;

import java.util.*;
import java.util.stream.Collectors;

public class Weapon {
    private String name = "";
    public Integer length = 0;
    private List<Move> options = Collections.emptyList();
    public Map<DamageTypes, Double> efficiencies = new HashMap<>();

    public Weapon() {
    }

    public Weapon(String name, Integer length) {
        this.name = name;
        this.length = length;
    }

    public Weapon withOptions(Move... options) {
        this.options = Arrays.asList(options);
        return this;
    }

    public Weapon withEfficiencies(Double slash, Double pierce, Double blunt) {
        efficiencies.put(DamageTypes.SLASH, slash);
        efficiencies.put(DamageTypes.PIERCE, pierce);
        efficiencies.put(DamageTypes.BLUNT, blunt);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Move> getOptions() {
        return options;
    }

    public void setOptions(List<Move> options) {
        this.options = options;
    }

    public Move getOptionByType(MoveTypes type) {
        List<Move> wanted = options.stream()
                .filter(move -> move.getType() == type)
                .collect(Collectors.toList());

        if (!wanted.isEmpty()) return wanted.get(0);
        return getOptionByType(MoveTypes.WAIT);
    }

    public Map<DamageTypes, Double> getEfficiencies() {
        return efficiencies;
    }

    public void setEfficiencies(Map<DamageTypes, Double> efficiencies) {
        this.efficiencies = efficiencies;
    }
}
