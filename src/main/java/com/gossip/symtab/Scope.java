package com.gossip.symtab;

/**
 * @author gaoxin.wei
 */
public interface Scope {

    public String getScopeName();

    public Scope getEnclosingScope();

    public void define(Symbol sym);

    public void define(String name, Symbol symbol);

    public Symbol resolve(String name);
}
