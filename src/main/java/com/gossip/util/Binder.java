package com.gossip.util;

import com.gossip.value.Value;

import java.util.function.BiFunction;

/**
 * Created by gaoxinwei on 2017/9/25.
 */
public class Binder {

    public static <T> BiFunction<Value<T>, Value<T>, Value<T>> lift(BiFunction<T, T, T> f) {
        return (left, right) -> left.flatMap(
            leftVal -> right.map(rightVal -> f.apply(leftVal, rightVal)));
    }

}
