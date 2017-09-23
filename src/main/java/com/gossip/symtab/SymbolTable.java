package com.gossip.symtab;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxin.wei
 */
public class SymbolTable implements Scope {

    Map<String, Symbol> symbols = new HashMap<String, Symbol>();

    public SymbolTable() {
        initTypeSystem();
    }

    private void initTypeSystem() {
        define(new BuiltInTypeSymbol("int"));
        define(new BuiltInTypeSymbol("float"));
    }

    public String getScopeName() {
        return "global";
    }

    public Scope getEnclosingScope() {
        return null;
    }

    public void define(Symbol sym) {
        symbols.put(sym.getName(), sym);
    }

    public Symbol resolve(String name) {
        return symbols.get(name);
    }
}
