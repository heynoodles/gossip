package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class BoolValue extends Value<Boolean> {

    public BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    @Override
    public Value create(Boolean value) {
        return value ? TRUE : FALSE;
    }
}
