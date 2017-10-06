package com.gossip.value.primitives;

import com.gossip.value.BoolValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class And extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value left = args.get(0);
        Value right = args.get(1);

        if (left instanceof BoolValue && right instanceof BoolValue) {
            return ((BoolValue)left).getValue() && ((BoolValue)right).getValue() ? Value.TRUE : Value.FALSE;
        }
        throw new Error("eval and fun error");
    }

    @Override
    public Value create(Object value) {
        return AND;
    }
}
