package com.gossip.symtab;

import com.gossip.ast.FunctionNode;

/**
 * @author gaoxin.wei
 * 一次方法执行的上下文, 包含实际数据
 */
public class ClosureScope extends CallScope {

    private FunctionNode functionNode;

    public ClosureScope(String scopeName, Scope enclosingScope) {
        super(scopeName, enclosingScope);
    }

    public FunctionNode getFunctionNode() {
        return functionNode;
    }

    public void setFunctionNode(FunctionNode functionNode) {
        this.functionNode = functionNode;
    }
}
