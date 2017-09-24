package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

import java.util.List;

/**
 * @author gaoxin.wei
 * (defun funName (params)
 *      body)
 */
public class FunctionNode extends HeteroAST {

    private NameNode funName;

    private List<NameNode> params;

    private HeteroAST body;

    public FunctionNode(Token root, NameNode funName, List<NameNode> params, HeteroAST body) {
        super(root);
        this.funName = funName;
        this.params = params;
        this.body = body;
    }

    public NameNode getFunName() {
        return funName;
    }

    public List<NameNode> getParams() {
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
