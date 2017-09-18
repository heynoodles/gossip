package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.visitor.GossipVisitor;

/**
 * @author gaoxin.wei
 */
public class AddNode extends HeteroAST {

    private HeteroAST left;
    private HeteroAST right;

    public AddNode(Token root, HeteroAST left, HeteroAST right) {
        super(root);
        this.left = left;
        this.right = right;
    }

    public Object visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }

    public HeteroAST getLeft() {
        return left;
    }

    public void setLeft(HeteroAST left) {
        this.left = left;
    }

    public HeteroAST getRight() {
        return right;
    }

    public void setRight(HeteroAST right) {
        this.right = right;
    }
}
