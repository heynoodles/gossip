package com.gossip.value.cons;

import com.gossip.value.Value;

/**
 * Created by gaoxinwei on 2017/9/29.
 */
public class ConsInner {

    private Value left;

    private Value right;

    public ConsInner() {}

    public ConsInner(Value left, Value right) {
        this.left = left;
        this.right = right;
    }

    public void setLeft(Value left) {
        this.left = left;
    }

    public void setRight(Value right) {
        this.right = right;
    }

    public Value getLeft() {
        return left;
    }

    public Value getRight() {
        return right;
    }
}
