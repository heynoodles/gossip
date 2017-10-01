package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class FloatValue extends Value<Double> {

    public FloatValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public Value create(Double value) {
        return new FloatValue(value);
    }
}
