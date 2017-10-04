package com.gossip.symtab;

/**
 * @author gaoxin.wei
 * 一次方法执行的上下文, 包含实际数据
 */
public class CallScope extends BaseScope {


    public CallScope(String scopeName, Scope enclosingScope) {
        super(scopeName, enclosingScope);
    }
}
