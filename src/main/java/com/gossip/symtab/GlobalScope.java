package com.gossip.symtab;

import com.gossip.value.primitives.*;

/**
 * @author gaoxin.wei
 */
public class GlobalScope extends BaseScope {

    public GlobalScope() {
        super("globals", null);
        symbols.put("+", new Add());
        symbols.put("-", new Sub());
        symbols.put(">", new Gt());
        symbols.put("<", new Lt());
        symbols.put("=", new Eq());
        symbols.put("print", new Print());
    }
}
