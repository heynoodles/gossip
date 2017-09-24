package com.gossip.symtab;

import com.gossip.ast.FunctionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxin.wei
 */
public class MethodSymbol extends Symbol implements Scope {

    private List<Symbol> orderedArgs = new ArrayList<Symbol>();

    private Scope enclosingScope;

    private FunctionNode functionNode;

    public MethodSymbol(String scopeName, Scope enclosingScope) {
        super(scopeName);
        this.enclosingScope = enclosingScope;
    }

    public FunctionNode getFunctionNode() {
        return functionNode;
    }

    public void setFunctionNode(FunctionNode functionNode) {
        this.functionNode = functionNode;
    }

    @Override
    public String getScopeName() {
        return name;
    }

    @Override
    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    @Override
    public void define(Symbol sym) {
        orderedArgs.add(sym);
    }

    @Override
    public Symbol resolve(String name) {
        for (Symbol orderedArg : orderedArgs) {
            if (name.equalsIgnoreCase(orderedArg.getName())) {
                return orderedArg;
            }
        }
        return null;
    }
}
