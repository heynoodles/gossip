package com.gossip.ast;

import com.gossip.ast.helper.VarAndValNode;
import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

import java.util.List;

/**
 * @author gaoxin.wei
 * (funName params)
 */
public class LetNode extends HeteroAST {

    private List<VarAndValNode> params;

    private HeteroAST body;

    public LetNode(Token root, List<VarAndValNode> params, HeteroAST body) {
        super(root);
        this.params = params;
        this.body = body;
    }

    public List<VarAndValNode> getParams() {
        return params;
    }

    public HeteroAST getBody() {
        return body;
    }

    @Override
    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
