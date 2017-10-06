package com.gossip.value.primitives;

import com.gossip.value.BoolValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class Not extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value value = args.get(0);

        if (value instanceof BoolValue) {
            return ((BoolValue)value).getValue() ? Value.FALSE : Value.TRUE;
        }
        throw new Error("eval and fun error");
    }

    @Override
    public Value create(Object value) {
        return NOT;
    }
}
