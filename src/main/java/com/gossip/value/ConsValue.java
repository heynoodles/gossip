package com.gossip.value;

import com.gossip.value.cons.Cons;

/**
 * Created by gaoxinwei on 2017/9/29.
 */
public class ConsValue extends Value<Cons> {

    public ConsValue(Cons cons) {
        this.value = cons;
    }

    @Override
    public String toString() {
        return "(" + this.value.getLeft() + " . " + this.value.getRight() + ")";
    }

    @Override
    public Value create(Cons value) {
        return new ConsValue(value);
    }
}
