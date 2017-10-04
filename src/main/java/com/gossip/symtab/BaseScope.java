package com.gossip.symtab;

import com.gossip.value.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxin.wei
 */
public class BaseScope implements Scope {

    Map<String, Value> symbols = new HashMap<String, Value>();

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


    @Override
    public void define(String name, Value value) {
        symbols.put(name, value);
    }

    public Value resolve(String name) {
        return symbols.get(name);
    }
}
