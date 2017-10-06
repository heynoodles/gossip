package com.gossip.symtab;

import com.gossip.value.Value;

/**
 * @author gaoxin.wei
 */
public class GlobalScope extends BaseScope {

    public GlobalScope() {
        super("globals", null);
        initPrimitiveFunctions();
    }

    private void initPrimitiveFunctions() {
        symbols.put("+", Value.ADD);
        symbols.put("-", Value.SUB);
        symbols.put("*", Value.MULT);
        symbols.put("/", Value.DIV);
        symbols.put(">", Value.GT);
        symbols.put("<", Value.LT);
        symbols.put("=", Value.EQ);
        symbols.put(">=", Value.GTE);
        symbols.put("<=", Value.LTE);
        symbols.put("and", Value.AND);
        symbols.put("or", Value.OR);
        symbols.put("not", Value.NOT);
        symbols.put("print", Value.PRINT);
        symbols.put("car", Value.CAR);
        symbols.put("cdr", Value.CDR);
        symbols.put("cons", Value.CONS);
    }
}
