package com.gossip.value;

import com.gossip.value.cons.ConsInner;

/**
 * Created by gaoxinwei on 2017/9/29.
 */
public class ConsValue extends Value<ConsInner> {

    public ConsValue(ConsInner cons) {
        this.value = cons;
    }

    @Override
    public String toString() {
        return "(" + this.value.getLeft() + " . " + this.value.getRight() + ")";
    }

    @Override
    public Value create(ConsInner value) {
        return new ConsValue(value);
    }
}
