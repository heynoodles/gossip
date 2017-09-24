package com.gossip.memory;

import com.gossip.ast.FunctionNode;

/**
 * @author gaoxin.wei
 */
public class FunctionSpace extends MemorySpace {

    private FunctionNode functionNode;

    public FunctionSpace(String name, FunctionNode functionNode) {
        super(name);
        this.functionNode = functionNode;
    }

    public FunctionNode getFunctionNode() {
        return functionNode;
    }
}
