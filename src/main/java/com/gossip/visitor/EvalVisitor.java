package com.gossip.visitor;

import com.gossip.ast.*;
import com.gossip.memory.FunctionSpace;
import com.gossip.memory.MemorySpace;
import com.gossip.symtab.MethodSymbol;
import com.gossip.symtab.Scope;
import com.gossip.symtab.Symbol;
import com.gossip.symtab.SymbolTable;
import com.gossip.util.Binder;
import com.gossip.value.IntValue;
import com.gossip.value.Value;

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

    private IntValue INT(IntNode node) {
        return new IntValue(Integer.valueOf(node.getToken().text));
    }

    private Value ADD(AddNode addNode) {
        return Binder.<Integer>lift(Math::addExact).apply(
            addNode.getLeft().visit(this),
            addNode.getRight().visit(this));
    }

    private Value PRINT(PrintNode printNode) {
        Value val = printNode.getParam().visit(this);
        System.out.println(val);
        return Value.VOID;
    }

    private Value MAIN(MainNode mainNode) {
        for (HeteroAST child : mainNode.getChildren()) {
            visit(child);
        }
        return Value.VOID;
    }

    private Value SETQ(SetqNode node) {
        NameNode var = node.getVar();
        Value val = node.getVal().visit(this);
        if (symbolTable.globalScope.resolve(var.getToken().text) == null) {
            throw new Error("cant resolve symbol: " + var.getToken().text);
        }
        globalSpace.put(var.getToken().text, val);
        return Value.VOID;
    }

    private Value NAME(NameNode node) {
        // 变量
        String text = node.getToken().text;
        Symbol symbol = symbolTable.getSymbolWithName(text);
        if (symbol == null) {
            throw new Error("cant resolve symbol: " + text);
        }
        MemorySpace memorySpace = getCurrentSpaceWithName(text);
        if (memorySpace == null) {
            throw new Error("cant resolve variable: " + text);
        }
        return memorySpace.get(text);
    }

    private Value CALL(CallNode callNode) {
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
        Value result = visit(functionNode.getBody());

        // 6. pop currentSpace
        stack.pop();

        // 7. pop currentScope
        symbolTable.setCurrentScope(previousScope);

        return result;
    }

    private Value GT(GTNode node) {
        Value left = node.getLeft().visit(this);
        Value right = node.getRight().visit(this);
        if (left instanceof IntValue && right instanceof IntValue) {
            return ((IntValue) left).getValue() > ((IntValue) right).getValue() ? Value.TRUE : Value.FALSE;
        }
        throw new Error("eval gt run");
    }

    private MemorySpace getCurrentSpaceWithName(String name) {
        if (stack.size() > 0 && stack.peek().get(name) != null) {
            return stack.peek();
        }
        if (globalSpace.get(name) != null) {
            return globalSpace;
        }
        return null;
    }

    public Value visit(HeteroAST node) {
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
        } else if (node instanceof GTNode) {
            return GT((GTNode)node);
        } else if (node instanceof CallNode) {
            return CALL((CallNode)node);
        } else {
            throw new Error("未知节点");
        }
    }


}
