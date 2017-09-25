package com.gossip.util;

import com.gossip.value.Value;

import java.util.function.Function;

/**
 * @author gaoxin.wei
 */
public interface Monad<T> {

    Value<T> map(Function<T, T> f);

    Value<T> flatMap(Function<T, Value<T>> f);
}
