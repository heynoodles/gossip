package com.gossip.symtab;

import com.gossip.ast.FunctionNode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaoxin.wei
 */
public class MethodSymbol extends Symbol implements Scope {

    private Map<String, Symbol> orderedArgs = new LinkedHashMap();

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
        orderedArgs.put(sym.getName(), sym);
    }

    @Override
    public void define(String name, Symbol symbol) {
        orderedArgs.put(name, symbol);
    }

    @Override
    public Symbol resolve(String name) {
       return orderedArgs.get(name);
    }
}
