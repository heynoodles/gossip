package com.gossip.value;

import com.gossip.util.Monad;

import java.util.function.Function;

/**
 * @author gaoxin.wei
 * 值类型抽象
 */
public abstract class Value<T> implements Monad<T> {

    protected T value;

    public T getValue() {
        return this.value;
    }

    public final static VoidValue VOID = new VoidValue();

    public abstract Value create(T value);

    @Override
    public Value map(Function<T, T> f) {
        return create(f.apply(value));
    }

    @Override
    public Value flatMap(Function<T, Value<T>> f) {
        return f.apply(value);
    }
}
