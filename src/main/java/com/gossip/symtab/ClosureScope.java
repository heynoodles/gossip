package com.gossip.symtab;

import com.gossip.ast.FunctionNode;

/**
 * @author gaoxin.wei
 * 一次闭包执行的上下文, 包含实际数据与方法
 */
public class ClosureScope extends CallScope {

    private FunctionNode functionNode;

    public ClosureScope(Scope enclosingScope, FunctionNode functionNode) {
        super("lambda", enclosingScope);
        this.functionNode = functionNode;
    }

    public FunctionNode getFunctionNode() {
        return functionNode;
    }

}
