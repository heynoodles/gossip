package com.gossip.ast;

import com.gossip.lexer.Token;
import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoxinwei on 2017/9/26.
 */
public class CondNode extends HeteroAST {

    private List<TestAndActionNode> blocks = new ArrayList<TestAndActionNode>();

    public CondNode(Token root, List<TestAndActionNode> blocks) {
        super(root);
        this.blocks = blocks;
    }

    public List<TestAndActionNode> getBlocks() {
        return blocks;
    }

    @Override
    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }

}
