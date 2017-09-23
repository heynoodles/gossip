package com.gossip.visitor;

import com.gossip.ast.*;
import com.gossip.memory.MemorySpace;
import com.gossip.symtab.SymbolTable;


/**
 * Created by gaoxinwei on 2017/9/18.
 */
public class EvalVisitor implements GossipVisitor {

    private MemorySpace globalSpace;

    private SymbolTable symbolTable;

    public EvalVisitor(SymbolTable symbolTable, MemorySpace memorySpace) {
        this.globalSpace = memorySpace;
        this.symbolTable = symbolTable;
    }

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

    private Object MAIN(MainNode mainNode) {
        for (HeteroAST child : mainNode.getChildren()) {
            visit(child);
        }
        return 1;
    }

    private Object SETQ(SetqNode node) {
        NameNode var = node.getVar();
        Object val = node.getVal().visit(this);
        if (symbolTable.resolve(var.getToken().text) == null) {
            throw new Error("cant resolve symbol: " + var.getToken().text);
        }
        globalSpace.put(var.getToken().text, val);
        return null;
    }

    private Object NAME(NameNode node) {
        if (symbolTable.resolve(node.getToken().text) == null) {
            throw new Error("cant resolve symbol: " + node.getToken().text);
        }
        return globalSpace.get(node.getToken().text);
    }

    public Object visit(HeteroAST node) {
        if (node instanceof MainNode) {
            return MAIN((MainNode) node);
        } else if (node instanceof IntNode) {
           return INT((IntNode)node);
        } else if (node instanceof AddNode) {
            return ADD((AddNode)node);
        } else if (node instanceof PrintNode) {
            return PRINT((PrintNode)node);
        } else if (node instanceof SetqNode) {
            return SETQ((SetqNode) node);
        } else if (node instanceof NameNode) {
            return NAME((NameNode)node);
        } else {
            throw new Error("未知节点");
        }
    }
}
