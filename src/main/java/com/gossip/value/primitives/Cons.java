package com.gossip.value.primitives;

import com.gossip.value.ConsValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;
import com.gossip.value.cons.ConsInner;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class Cons extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value leftVal = args.get(0);
        Value rightVal = args.get(1);
        return new ConsValue(new ConsInner(leftVal, rightVal));
    }

    @Override
    public Value create(Object value) {
        return CONS;
    }
}
