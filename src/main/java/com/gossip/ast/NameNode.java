package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.visitor.GossipVisitor;

/**
 * @author gaoxin.wei
 */
public class NameNode extends HeteroAST {

    public NameNode(Token token) {
        super(token);
    }

    @Override
    public Object visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
