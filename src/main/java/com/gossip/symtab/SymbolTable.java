package com.gossip.symtab;

import com.gossip.value.Value;

/**
 * @author gaoxin.wei
 */
public class SymbolTable {

    public GlobalScope globalScope = new GlobalScope();

    private Scope currentScope = globalScope;

    public Value getSymbolWithName(String name) {
        Value resolve = this.currentScope.resolve(name);
        if (resolve != null) {
            return resolve;
        }
        for (Scope currentScope1 = this.currentScope; currentScope1.getEnclosingScope() != null;
             currentScope1 = currentScope1.getEnclosingScope()) {
            Value resolve1 = currentScope1.getEnclosingScope().resolve(name);
            if (resolve1 != null) {
                return resolve1;
            }
        }
        return null;
    }

    public GlobalScope getGlobalScope() {
        return globalScope;
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    public void setCurrentScope(Scope currentScope) {
        this.currentScope = currentScope;
    }
}
