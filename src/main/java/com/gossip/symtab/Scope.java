package com.gossip.symtab;

import com.gossip.value.Value;

/**
 * @author gaoxin.wei
 */
public interface Scope {

    String getScopeName();

    Scope getEnclosingScope();

    void define(String name, Value symbol);

    Value resolve(String name);
}
