package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

/**
 * @author gaoxin.wei
 */
public class FloatNode extends HeteroAST {

    public FloatNode(Token token) {
        super(token);
    }

    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
