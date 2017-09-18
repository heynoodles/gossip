package com.gossip.visitor;

import com.gossip.ast.AddNode;
import com.gossip.ast.HeteroAST;
import com.gossip.ast.IntNode;

/**
 * @author gaoxin.wei
 */
public class PrintVisitor implements GossipVisitor {

    public void visit(IntNode intNode) {
        System.out.println(intNode.getToken());
    }

    public void visit(AddNode addNode) {
        System.out.println(addNode.getToken());
        addNode.getLeft().visit(this);
        addNode.getRight().visit(this);
    }

    public Object visit(HeteroAST node) {
        if (node instanceof IntNode) {
            visit((IntNode)node);
        } else if (node instanceof AddNode) {
            visit((AddNode)node);
        }
        return null;
    }
}
