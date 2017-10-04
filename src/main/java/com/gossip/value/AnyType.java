package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class AnyType extends Value {

    public AnyType() {}

    public AnyType(Object text) {
        this.value = text;
    }

    @Override
    public Value create(Object value) {
        return null;
    }
}
