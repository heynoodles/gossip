package com.gossip.symtab;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxin.wei
 */
public class BaseScope implements Scope {

    Map<String, Symbol> symbols = new HashMap<String, Symbol>();

    private Scope enclosingScope;

    private String scopeName;

    public BaseScope(String scopeName, Scope enclosingScope) {
        this.scopeName = scopeName;
        this.enclosingScope = enclosingScope;
    }

    public String getScopeName() {
        return scopeName;
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public void define(Symbol sym) {
        symbols.put(sym.getName(), sym);
    }

    public Symbol resolve(String name) {
        return symbols.get(name);
    }
}
