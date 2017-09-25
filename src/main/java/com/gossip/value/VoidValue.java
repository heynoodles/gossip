package com.gossip.value;

/**
 * @author gaoxin.wei
 * 值类型抽象
 */
public class VoidValue extends Value {

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public Value create(Object value) {
        return Value.VOID;
    }
}
