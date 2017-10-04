package com.gossip.symtab;

import com.gossip.ast.FunctionNode;
import com.gossip.ast.NameNode;
import com.gossip.value.AnyType;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 * 只包含方法模板，不包含实际调用的任何信息
 */
public class MethodSymbol extends Value implements Scope {

    private String name;

    private Scope enclosingScope;

    private FunctionNode functionNode;

    public MethodSymbol(String scopeName, Scope enclosingScope) {
        this.name = scopeName;
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
    public void define(String name, Value symbol) {
        throw new Error("unsupported method");
    }

    @Override
    public Value resolve(String name) {
        List<NameNode> params = functionNode.getParams();
        if (params != null) {
            for (NameNode param : params) {
                if (name.equalsIgnoreCase(param.getToken().text)) {
                    return new AnyType(param.getToken().text);
                }
            }
        }
        return null;
    }

    @Override
    public Value create(Object value) {
        return null;
    }
}
