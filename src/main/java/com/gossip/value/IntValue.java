package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class IntValue extends Value<Integer> {

    public IntValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Value create(Integer value) {
        return new IntValue(value);
    }
}
