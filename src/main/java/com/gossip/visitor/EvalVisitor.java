package com.gossip.visitor;

import com.gossip.ast.*;
import com.gossip.memory.FunctionSpace;
import com.gossip.memory.MemorySpace;
import com.gossip.symtab.MethodSymbol;
import com.gossip.symtab.Scope;
import com.gossip.symtab.Symbol;
import com.gossip.symtab.SymbolTable;

import java.util.Stack;


/**
 * Created by gaoxinwei on 2017/9/18.
 */
public class EvalVisitor implements GossipVisitor {

    private MemorySpace globalSpace;

    private SymbolTable symbolTable;

    private Stack<FunctionSpace> stack = new Stack<FunctionSpace>();

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
        if (symbolTable.globalScope.resolve(var.getToken().text) == null) {
            throw new Error("cant resolve symbol: " + var.getToken().text);
        }
        globalSpace.put(var.getToken().text, val);
        return null;
    }

    private Object NAME(NameNode node) {
        String text = node.getToken().text;
        Symbol symbol = symbolTable.getSymbolWithName(text);
        if (symbol == null) {
            throw new Error("cant resolve symbol: " + text);
        }
            // 变量
        return getCurrentSpace().get(text);
    }

    private Object CALL(CallNode callNode) {
        // 1. 获取functionNode
        String funcName = callNode.getToken().text;
        Symbol symbol = symbolTable.getSymbolWithName(funcName);
        if (symbol == null) {
            throw new Error("unsupported symbol type");
        }
        MethodSymbol methodSymbol = (MethodSymbol) symbol;
        FunctionNode functionNode = methodSymbol.getFunctionNode();

        // 2. 设置currentScope
        Scope previousScope = symbolTable.getCurrentScope();
        symbolTable.setCurrentScope(methodSymbol);

        // 3. 设置currentSpace
        FunctionSpace fs = new FunctionSpace(funcName, functionNode);
        stack.push(fs);

        // 4. prepare args in currentSpace
        for (int i = 0; i < functionNode.getParams().size(); i++) {
            NameNode nameNode = functionNode.getParams().get(i);
            if (i < callNode.getParams().size()) {
                HeteroAST paramNode = callNode.getParams().get(i);
                fs.put(nameNode.getToken().text, visit(paramNode));
            }
        }

        // 5. call
        Object result = visit(functionNode.getBody());

        // 6. pop currentSpace
        stack.pop();

        // 7. pop currentScope
        symbolTable.setCurrentScope(previousScope);

        return result;
    }

    private MemorySpace getCurrentSpace() {
        if (stack.size() > 0) {
            return stack.peek();
        }
        return globalSpace;
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
        } else if (node instanceof CallNode) {
            return CALL((CallNode)node);
        } else {
            throw new Error("未知节点");
        }
    }

}
