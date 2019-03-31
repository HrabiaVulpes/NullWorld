package combat_data;

import java.util.*;

public class Weapon {
    private String name = "";
    private Integer length = 0;
    private List<Move> options = Collections.emptyList();
    private Map<DamageTypes, Double> efficiencies = new HashMap<>();

    public Weapon() { }

    public Weapon(String name, Integer length) {
        this.name = name;
        this.length = length;
    }

    public Weapon withOptions(Move... options){
        this.options = Arrays.asList(options);
        return this;
    }

    public Weapon withEfficiencies(Double slash, Double pierce, Double blunt){
        efficiencies.put(DamageTypes.SLASH, slash);
        efficiencies.put(DamageTypes.PIERCE, pierce);
        efficiencies.put(DamageTypes.BLUNT, blunt);
        return this;
    }

    public String getName() {
        return name;
    }

    public Integer getLength() {
        return length;
    }

    public List<Move> getOptions() {
        return options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setOptions(List<Move> options) {
        this.options = options;
    }

    public Map<DamageTypes, Double> getEfficiencies() {
        return efficiencies;
    }

    public void setEfficiencies(Map<DamageTypes, Double> efficiencies) {
        this.efficiencies = efficiencies;
    }
}
