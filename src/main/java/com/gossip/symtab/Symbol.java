package com.gossip.symtab;

/**
 * @author gaoxin.wei
 */
public class Symbol {
    String name;

    public Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }
}
