package com.gossip.value;

/**
 * @author gaoxin.wei
 */
public class FuncValue extends Value<String> {

    public FuncValue(String funcName) {
        this.value = funcName;
    }

    @Override
    public Value create(String value) {
        return null;
    }
}
