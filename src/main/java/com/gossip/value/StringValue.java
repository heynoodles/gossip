package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class StringValue extends Value<String> {

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
    public Value create(String value) {
        return new StringValue(value);
    }
}
