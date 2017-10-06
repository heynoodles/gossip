package com.gossip.value.primitives;

import com.gossip.util.Binder;
import com.gossip.value.FloatValue;
import com.gossip.value.IntValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class Add extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value left = args.get(0);
        Value right = args.get(1);

        if (left instanceof IntValue && right instanceof IntValue) {
            return Binder.<Integer, Integer>lift(Math::addExact).apply(left, right);
        }

        if (left instanceof FloatValue && right instanceof FloatValue) {
            return Binder.<Double, Double>lift((v1, v2) -> v1 + v2).apply(left, right);
        }

        if (left instanceof IntValue && right instanceof FloatValue) {
            return Binder.<Integer, Double>lift((v1, v2) -> v1 + v2).apply(left, right);
        }

        if (left instanceof FloatValue && right instanceof IntValue) {
            return Binder.<Integer, Double>lift((v1, v2) -> v1 + v2).apply(right, left);
        }

        throw new Error("eval add fun error");
    }
}
