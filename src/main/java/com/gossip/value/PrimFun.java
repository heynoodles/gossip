package com.gossip.value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public abstract class PrimFun extends Value {

    public abstract Value apply(List<Value> args);

    @Override
    public Value create(Object value) {
        return null;
    }
}
