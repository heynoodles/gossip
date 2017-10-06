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
        symbols.put("*", new Mult());
        symbols.put("/", new Div());
        symbols.put(">", new Gt());
        symbols.put("<", new Lt());
        symbols.put("=", new Eq());
        symbols.put(">=", new GtE());
        symbols.put("<=", new LtE());
        symbols.put("and", new And());
        symbols.put("or", new Or());
        symbols.put("not", new Not());
        symbols.put("print", new Print());
    }
}
