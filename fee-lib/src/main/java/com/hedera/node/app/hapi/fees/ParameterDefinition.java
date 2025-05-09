package com.hedera.node.app.hapi.fees;

public class ParameterDefinition {
    public String name;
    public String type;
    public Object defaultValue;
    public int min;
    public int max;
    public String prompt;

    public ParameterDefinition(String name, String type, Object defaultValue, int min, int max, String prompt) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.prompt = prompt;
    }
}
