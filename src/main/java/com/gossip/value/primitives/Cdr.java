package com.gossip.value.primitives;

import com.gossip.value.ConsValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class Cdr extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value val = args.get(0);
        if (val instanceof ConsValue) {
            ConsValue consValue = (ConsValue) val;
            return consValue.getValue().getRight();
        }
        throw new Error("eval cdr fun error");
    }

    @Override
    public Value create(Object value) {
        return CDR;
    }
}
