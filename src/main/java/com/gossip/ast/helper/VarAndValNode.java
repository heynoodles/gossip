package com.gossip.ast.helper;

import com.gossip.ast.HeteroAST;
import com.gossip.ast.NameNode;

/**
 * @author gaoxin.wei
 */
public class VarAndValNode {

    private NameNode var;
    private HeteroAST val;

    public VarAndValNode(NameNode var, HeteroAST val) {
        this.var = var;
        this.val = val;
    }

    public NameNode getVar() {
        return var;
    }

    public HeteroAST getVal() {
        return val;
    }
}
