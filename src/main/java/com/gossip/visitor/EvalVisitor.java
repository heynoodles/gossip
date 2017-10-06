package com.gossip.visitor;

import com.gossip.ast.*;
import com.gossip.ast.helper.TestAndActionNode;
import com.gossip.ast.helper.VarAndValNode;
import com.gossip.symtab.CallScope;
import com.gossip.symtab.ClosureScope;
import com.gossip.symtab.Scope;
import com.gossip.symtab.SymbolTable;
import com.gossip.value.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gaoxinwei on 2017/9/18.
 */
public class EvalVisitor implements GossipVisitor {

    private SymbolTable symbolTable;

    public EvalVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    private IntValue INT(IntNode node) {
        return new IntValue(Integer.valueOf(node.getToken().text));
    }

    private FloatValue FLOAT(FloatNode node) {
        return new FloatValue(Double.valueOf(node.getToken().text));
    }

    private StringValue STRING(StringNode node) {
        return new StringValue(node.getToken().text);
    }

    private Value MAIN(MainNode mainNode) {
        Value result = Value.VOID;
        for (HeteroAST child : mainNode.getChildren()) {
           result = visit(child);
        }
        return result;
    }

    private Value SETQ(SetqNode node) {
        NameNode var = node.getVar();
        Value val = node.getVal().visit(this);
        if (symbolTable.globalScope.resolve(var.getToken().text) == null) {
            throw new Error("cant resolve symbol: " + var.getToken().text);
        }
        symbolTable.globalScope.define(var.getToken().text, val);
        return Value.VOID;
    }

    private Value NAME(NameNode node) {
        String text = node.getToken().text;
        return symbolTable.getSymbolWithName(text);
    }

    private Value LET(LetNode letNode) {

        Scope previousScope = symbolTable.getCurrentScope();

        // (let binder block)
        CallScope callScope = new CallScope("", symbolTable.getCurrentScope());
        // prepare args in currentSpace
        for (VarAndValNode varAndValNode : letNode.getParams()) {
            callScope.define(varAndValNode.getVar().getToken().text, varAndValNode.getVal().visit(this));
        }

        // 设置currentSpace
        symbolTable.setCurrentScope(callScope);

        Value result = letNode.getBody().visit(this);

        // pop currentSpace
        symbolTable.setCurrentScope(previousScope);

        return result;
    }

    private Value FUNCTION(FunctionNode node) {
        ClosureScope closure = new ClosureScope(symbolTable.getCurrentScope(), node);
        return new ClosureValue(closure);
    }

    private Value CALL(CallNode callNode) {

        HeteroAST operator = callNode.getOperator();
        FunctionValue methodSymbol = null;
        CallScope callScope = null;
        FunctionNode functionNode = null;

        if (operator instanceof NameNode) {
            String funcName = operator.getToken().text;
            Value symbol = symbolTable.getSymbolWithName(funcName);
            // case 1: primitive func
            if (symbol instanceof PrimFun) {
                PrimFun primFun = (PrimFun) symbol;
                // prepare args
                List<Value> args = new ArrayList<Value>();
                for (HeteroAST heteroAST : callNode.getParams()) {
                    Value arg = heteroAST.visit(this);
                    args.add(arg);
                }
                return primFun.apply(args);
            } else {
                // case 2: custom func
                methodSymbol = (FunctionValue) symbolTable.getSymbolWithName(funcName);
                if (methodSymbol == null) {
                    throw new Error("unsupported symbol type");
                }
                callScope = new CallScope(funcName, symbolTable.getCurrentScope());
                functionNode = methodSymbol.getValue();
            }
        } else if (operator instanceof CallNode) {
            // case 3: still list
            Value val = CALL((CallNode) operator);
            ClosureValue funcValue = (ClosureValue)val;
            callScope = funcValue.getValue();
            functionNode = ((ClosureScope)callScope).getFunctionNode();
        }

        // prepare args in currentSpace
        for (int i = 0; i < functionNode.getParams().size(); i++) {
            NameNode nameNode = functionNode.getParams().get(i);
            if (i < callNode.getParams().size()) {
                HeteroAST paramNode = callNode.getParams().get(i);
                Value val = visit(paramNode);
                callScope.define(nameNode.getToken().text, val);
            }
        }

        // 设置currentScope
        Scope previousScope = symbolTable.getCurrentScope();
        symbolTable.setCurrentScope(callScope);

        // call
        Value result = visit(functionNode.getBody());

        // pop currentScope
        symbolTable.setCurrentScope(previousScope);

        return result;
    }

    private Value COND(CondNode node) {
        for (TestAndActionNode block : node.getBlocks()) {
            BoolValue testVal = (BoolValue) block.getTest().visit(this);
            if (testVal.getValue()) {
                return block.getAction().visit(this);
            }
        }
        return Value.VOID;
    }

    public Value visit(HeteroAST node) {
        if (node instanceof MainNode) {
            return MAIN((MainNode) node);
        } else if (node instanceof IntNode) {
           return INT((IntNode)node);
        } else if (node instanceof FloatNode) {
            return FLOAT((FloatNode)node);
        } else if (node instanceof StringNode) {
            return STRING((StringNode)node);
        } else if (node instanceof SetqNode) {
            return SETQ((SetqNode) node);
        } else if (node instanceof NameNode) {
            return NAME((NameNode)node);
        } else if (node instanceof CallNode) {
            return CALL((CallNode)node);
        } else if (node instanceof CondNode) {
            return COND((CondNode)node);
        } else if (node instanceof LetNode) {
            return LET((LetNode)node);
        } else if (node instanceof FunctionNode) {
            return FUNCTION((FunctionNode)node);
        } else {
            throw new Error("未知节点");
        }
    }



}
