package com.gossip.value.primitives;

import com.gossip.value.FloatValue;
import com.gossip.value.IntValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class LtE extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value left = args.get(0);
        Value right = args.get(1);
        if (left instanceof IntValue && right instanceof IntValue) {
            return ((IntValue) left).getValue() <= ((IntValue) right).getValue() ? Value.TRUE : Value.FALSE;
        }
        if (left instanceof FloatValue && right instanceof FloatValue) {
            return ((FloatValue) left).getValue() <= ((FloatValue) right).getValue() ? Value.TRUE : Value.FALSE;
        }
        if (left instanceof IntValue && right instanceof FloatValue) {
            return ((IntValue) left).getValue() <= ((FloatValue) right).getValue() ? Value.TRUE : Value.FALSE;
        }
        if (left instanceof FloatValue && right instanceof IntValue) {
            return ((FloatValue) left).getValue() <= ((IntValue) right).getValue() ? Value.TRUE : Value.FALSE;
        }
        throw new Error("eval gte fun error");
    }
}
