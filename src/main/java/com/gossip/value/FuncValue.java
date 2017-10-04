package com.gossip.value;

import com.gossip.symtab.CallScope;

/**
 * @author gaoxin.wei
 */
public class FuncValue extends Value<String> {

    // 返回的函数 需要把当时的环境也带出去
    private CallScope scope;

    public FuncValue(String funcName) {
        this.value = funcName;
    }

    public CallScope getScope() {
        return scope;
    }

    public void setScope(CallScope scope) {
        this.scope = scope;
    }

    @Override
    public Value create(String value) {
        return null;
    }
}
