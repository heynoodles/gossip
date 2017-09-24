package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class IntValue extends Value {

    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
