package com.gossip.value;

import com.gossip.util.Monad;
import com.gossip.value.primitives.*;

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
    public final static BoolValue TRUE = new BoolValue(true);
    public final static BoolValue FALSE = new BoolValue(false);
    public final static Add ADD = new Add();
    public final static Sub SUB = new Sub();
    public final static And AND = new And();
    public final static Div DIV = new Div();
    public final static Eq  EQ = new Eq();
    public final static Gt GT =new Gt();
    public final static GtE GTE =new GtE();
    public final static Lt LT = new Lt();
    public final static LtE LTE = new LtE();
    public final static Mult MULT = new Mult();
    public final static Not NOT = new Not();
    public final static Or OR = new Or();
    public final static Print PRINT = new Print();
    public final static Car CAR = new Car();
    public final static Cdr CDR = new Cdr();
    public final static Cons CONS = new Cons();

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
