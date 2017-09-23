package com.gossip.symtab;

/**
 * @author gaoxin.wei
 */
public class Symbol {
    String name;
    Type type;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        if (type != null) {
            return '<' + getName() + ": " + type + '>';
        }
        return getName();
    }
}
