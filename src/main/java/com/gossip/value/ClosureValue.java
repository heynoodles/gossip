package com.gossip.value;

import com.gossip.symtab.ClosureScope;

/**
 * @author gaoxin.wei
 * 返回闭包 需要把当时的环境也带出去
 */
public class ClosureValue extends Value<ClosureScope> {

    public ClosureValue(ClosureScope scope) {
        this.value = scope;
    }

    @Override
    public Value create(ClosureScope value) {
        return null;
    }
}
