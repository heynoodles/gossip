package com.gossip.symtab;

/**
 * @author gaoxin.wei
 */
public class SymbolTable {

    public GlobalScope globalScope = new GlobalScope();

    private Scope currentScope = globalScope;

    public Symbol getSymbolWithName(String name) {
        Symbol resolve = this.currentScope.resolve(name);
        if (resolve != null) {
            return resolve;
        }
        for (Scope currentScope = this.currentScope; currentScope.getEnclosingScope() != null;
             currentScope = currentScope.getEnclosingScope()) {
            Symbol resolve1 = currentScope.getEnclosingScope().resolve(name);
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
