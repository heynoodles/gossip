package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.visitor.GossipVisitor;

/**
 * @author gaoxin.wei
 */
public class PrintNode extends HeteroAST {

    private HeteroAST param;

    public PrintNode(Token root, HeteroAST param) {
        super(root);
        this.param = param;
    }

    public HeteroAST getParam() {
        return param;
    }

    public void setParam(HeteroAST param) {
        this.param = param;
    }

    public Object visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
