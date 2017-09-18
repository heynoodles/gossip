package com.gossip.visitor;

import com.gossip.ast.AddNode;
import com.gossip.ast.HeteroAST;
import com.gossip.ast.IntNode;
import com.gossip.ast.PrintNode;

/**
 * Created by gaoxinwei on 2017/9/18.
 */
public class EvalVisitor implements GossipVisitor {

    private Object INT(IntNode node) {
        return Integer.valueOf(node.getToken().text);
    }

    private Object ADD(AddNode addNode) {
        Integer leftVal = (Integer) addNode.getLeft().visit(this);
        Integer rightVal = (Integer) addNode.getRight().visit(this);
        return leftVal + rightVal;
    }

    private Object PRINT(PrintNode printNode) {
        Object val = printNode.getParam().visit(this);
        System.out.println(val);
        return val;
    }

    public Object visit(HeteroAST node) {
        if (node instanceof IntNode) {
           return INT((IntNode)node);
        } else if (node instanceof AddNode) {
            return ADD((AddNode)node);
        } else if (node instanceof PrintNode) {
            return PRINT((PrintNode)node);
        } else {
            throw new Error("未知节点");
        }
    }
}
