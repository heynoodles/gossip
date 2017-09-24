package com.gossip.ast;

import com.gossip.value.Value;
import com.gossip.visitor.GossipVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoxin.wei
 * 程序入口
 */
public class MainNode extends HeteroAST {

    private List<HeteroAST> children;

    public void addChild(HeteroAST child) {
        if (children == null)
            this.children = new ArrayList<HeteroAST>();
        children.add(child);
    }

    public List<HeteroAST> getChildren() {
        return children;
    }

    public void setChildren(List<HeteroAST> children) {
        this.children = children;
    }

    @Override
    public Value visit(GossipVisitor visitor) {
        return visitor.visit(this);
    }
}
