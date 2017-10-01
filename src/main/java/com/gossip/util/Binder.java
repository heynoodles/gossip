package com.gossip.util;

import com.gossip.value.Value;

import java.util.function.BiFunction;

/**
 * Created by gaoxinwei on 2017/9/25.
 */
public class Binder {

    public static <L, R> BiFunction<Value<L>, Value<R>, Value<R>> lift(BiFunction<L, R, R> f) {
        return (left, right) -> left.flatMap(
                leftVal -> right.map(rightVal -> f.apply(leftVal, rightVal)));
    }
}
