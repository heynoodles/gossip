package com.gossip.symtab;

/**
 * @author gaoxin.wei
 */
public interface Scope {

    public String getScopeName();

    public Scope getEnclosingScope();

    public void define(Symbol sym);

    public Symbol resolve(String name);
}
