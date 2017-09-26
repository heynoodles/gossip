package com.gossip.ast;

/**
 * Created by gaoxinwei on 2017/9/26.
 */
public class TestAndActionNode {

    private HeteroAST test;

    private HeteroAST action;

    public TestAndActionNode(HeteroAST test, HeteroAST action) {
        this.test = test;
        this.action = action;
    }

    public HeteroAST getTest() {
        return test;
    }

    public HeteroAST getAction() {
        return action;
    }
}
