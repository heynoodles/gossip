package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

import java.util.List;

/**
 * @author gaoxin.wei
 * (funName params)
 */
public class CallNode extends HeteroAST {

    private List<HeteroAST> params;

    public CallNode(Token root, List<HeteroAST> params) {
        super(root);
        this.params = params;
    }

    public List<HeteroAST> getParams() {
        return params;
    }

    @Override
    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
