package com.gossip.util;

import com.gossip.value.IntValue;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by gaoxinwei on 2017/9/25.
 */
public class Binder {

    public static <A, B> BiFunction<A, A, B> bind(BiFunction<B, B, B> fun, Function<A, B> unit) {
        return (b1, b2) -> fun.apply(unit.apply(b1), unit.apply(b2));
    }

}
