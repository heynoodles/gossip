package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

/**
 * @author gaoxin.wei
 */
public class AddNode extends BiNode {

    public AddNode(Token root, HeteroAST left, HeteroAST right) {
        super(root, left, right);
    }

    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }

}
