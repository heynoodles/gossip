package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

/**
 * Created by gaoxinwei on 2017/9/29.
 */
public class CarNode extends HeteroAST {

    private HeteroAST value;

    public CarNode(Token root, HeteroAST value) {
        super(root);
        this.value = value;
    }

    public HeteroAST getValue() {
        return value;
    }

    @Override
    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
