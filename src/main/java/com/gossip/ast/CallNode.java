package com.gossip.ast;

import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

import java.util.List;

/**
 * @author gaoxin.wei
 * (funName params)
 */
public class CallNode extends HeteroAST {

    private HeteroAST operator;

    private List<HeteroAST> params;

    public CallNode(HeteroAST operator, List<HeteroAST> params) {
        this.params = params;
        this.operator = operator;
    }

    public List<HeteroAST> getParams() {
        return params;
    }

    public HeteroAST getOperator() {
        return operator;
    }

    @Override
    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
