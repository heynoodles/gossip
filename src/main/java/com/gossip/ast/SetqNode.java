package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.visitor.GossipVisitor;

/**
 * @author gaoxin.wei
 */
public class SetqNode extends HeteroAST {

    private NameNode var;
    private HeteroAST val;

    public SetqNode(Token root, NameNode var, HeteroAST val) {
        super(root);
        this.var = var;
        this.val = val;
    }

    public Object visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }

    public NameNode getVar() {
        return var;
    }

    public void setVar(NameNode var) {
        this.var = var;
    }

    public HeteroAST getVal() {
        return val;
    }

    public void setVal(HeteroAST val) {
        this.val = val;
    }
}
