package com.gossip.visitor;

import com.gossip.ast.*;
import com.gossip.ast.helper.TestAndActionNode;
import com.gossip.ast.helper.VarAndValNode;
import com.gossip.symtab.*;
import com.gossip.value.*;
import com.gossip.value.cons.Cons;

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


    private Value CONS(ConsNode consNode) {
        Value leftVal = consNode.getLeft().visit(this);
        Value rightVal = consNode.getRight().visit(this);
        return new ConsValue(new Cons(leftVal, rightVal));
    }

    private Value CAR(CarNode node) {
        Value val = node.getValue().visit(this);
        ConsValue consValue = (ConsValue)val;
        return consValue.getValue().getLeft();
    }

    private Value CDR(CdrNode node) {
        Value val = node.getValue().visit(this);
        ConsValue consValue = (ConsValue)val;
        return consValue.getValue().getRight();
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
        FuncValue funcValue = new FuncValue("lambda");
        ClosureScope closure = new ClosureScope("lambda", symbolTable.getCurrentScope());
        closure.setFunctionNode(node);
        funcValue.setScope(closure);
        return funcValue;
    }

    private Value CALL(CallNode callNode) {
        // 获取functionNode

        HeteroAST operator = callNode.getOperator();
        MethodSymbol methodSymbol = null;
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
                methodSymbol = (MethodSymbol) symbolTable.getSymbolWithName(funcName);
                if (methodSymbol == null) {
                    throw new Error("unsupported symbol type");
                }
                callScope = new CallScope(funcName, symbolTable.getCurrentScope());
                functionNode = methodSymbol.getFunctionNode();
            }
        } else if (operator instanceof CallNode) {
            // case 3: still list
            Value val = CALL((CallNode) operator);
            FuncValue funcValue = (FuncValue)val;
            callScope = funcValue.getScope();
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
        } else if (node instanceof ConsNode) {
            return CONS((ConsNode)node);
        } else if (node instanceof CarNode) {
            return CAR((CarNode)node);
        } else if (node instanceof CdrNode) {
            return CDR((CdrNode)node);
        } else if (node instanceof LetNode) {
            return LET((LetNode)node);
        } else if (node instanceof FunctionNode) {
            return FUNCTION((FunctionNode)node);
        } else {
            throw new Error("未知节点");
        }
    }



}
