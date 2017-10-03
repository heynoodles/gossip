package com.gossip.visitor;

import com.gossip.ast.*;
import com.gossip.memory.FunctionSpace;
import com.gossip.memory.MemorySpace;
import com.gossip.symtab.MethodSymbol;
import com.gossip.symtab.Scope;
import com.gossip.symtab.Symbol;
import com.gossip.symtab.SymbolTable;
import com.gossip.util.Binder;
import com.gossip.value.*;
import com.gossip.value.cons.Cons;

import java.util.*;


/**
 * Created by gaoxinwei on 2017/9/18.
 */
public class EvalVisitor implements GossipVisitor {

    private MemorySpace globalSpace;

    private SymbolTable symbolTable;

    private List<MemorySpace> stack = new ArrayList<>();

    public EvalVisitor(SymbolTable symbolTable, MemorySpace memorySpace) {
        this.globalSpace = memorySpace;
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

    private Value ADD(AddNode addNode) {
        Value left = addNode.getLeft().visit(this);
        Value right = addNode.getRight().visit(this);

        if (left instanceof IntValue && right instanceof IntValue) {
            return Binder.<Integer, Integer>lift(Math::addExact).apply(left, right);
        }

        if (left instanceof FloatValue && right instanceof FloatValue) {
            return Binder.<Double, Double>lift((v1, v2) -> v1 + v2).apply(left, right);
        }

        if (left instanceof IntValue && right instanceof FloatValue) {
            return Binder.<Integer, Double>lift((v1, v2) -> v1 + v2).apply(left, right);
        }

        if (left instanceof FloatValue && right instanceof IntValue) {
            return Binder.<Integer, Double>lift((v1, v2) -> v1 + v2).apply(right, left);
        }

        throw new Error("eval add node error");
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

    private Value SUBTRACT(SubtractNode minusNode) {
        Value left = minusNode.getLeft().visit(this);
        Value right = minusNode.getRight().visit(this);

        if (left instanceof IntValue && right instanceof IntValue) {
            return Binder.<Integer, Integer>lift(Math::subtractExact).apply(left, right);
        }

        if (left instanceof FloatValue && right instanceof FloatValue) {
            return Binder.<Double, Double>lift((v1, v2) -> v1 - v2).apply(left, right);
        }

        if (left instanceof IntValue && right instanceof FloatValue) {
            return Binder.<Integer, Double>lift((v1, v2) -> v1 - v2).apply(left, right);
        }

        if (left instanceof FloatValue && right instanceof IntValue) {
            return Binder.<Integer, Double>lift((v1, v2) -> v1 - v2).apply(right, left);
        }

        throw new Error("eval subtract node error");
    }

    private Value PRINT(PrintNode printNode) {
        Value val = printNode.getParam().visit(this);
        System.out.println(val);
        return Value.VOID;
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
        globalSpace.put(var.getToken().text, val);
        return Value.VOID;
    }

    private Value NAME(NameNode node) {
        String text = node.getToken().text;
        MemorySpace memorySpace = getCurrentSpaceWithName(text);
        if (memorySpace != null) {
            // 变量
            return memorySpace.get(text);
        } else {
            Symbol resolve = symbolTable.getCurrentScope().resolve(text);
            if (resolve != null) {
                // 方法
                return new FuncValue(resolve.getName());
            } else
                throw new Error("cant resolve variable: " + text);
        }

    }

    private Value LET(LetNode letNode) {
        // (let binder block)
        MemorySpace memorySpace = new MemorySpace("");
        // prepare args in currentSpace
        for (VarAndValNode varAndValNode : letNode.getParams()) {
            memorySpace.put(varAndValNode.getVar().getToken().text, varAndValNode.getVal().visit(this));
        }

        // 设置currentSpace
        stack.add(memorySpace);
        Value result = letNode.getBody().visit(this);

        // pop currentSpace
        stack.remove(memorySpace);

        return result;
    }

    private Value FUNCTION(FunctionNode node) {
        FuncValue funcValue = new FuncValue("lambda");
        MethodSymbol methodSymbol = new MethodSymbol("lambda", symbolTable.getCurrentScope());
        methodSymbol.setFunctionNode(node);
        funcValue.setScope(methodSymbol);
        return funcValue;
    }

    private Value CALL(CallNode callNode) {
        // 获取functionNode
        // String funcName = callNode.getToken().text;
        FunctionNode functionNode = null;
        FunctionSpace fs = null;
        MethodSymbol methodSymbol = null;
        HeteroAST operator = callNode.getOperator();

        if (operator instanceof NameNode) {
            String funcName = operator.getToken().text;
            Symbol symbol = symbolTable.getSymbolWithName(funcName);
            if (symbol == null) {
                throw new Error("unsupported symbol type");
            }
            methodSymbol = (MethodSymbol) symbol;
            fs = new FunctionSpace(funcName, methodSymbol.getFunctionNode());
        } else if (operator instanceof CallNode) {
            Value val = CALL((CallNode) operator);
            FuncValue funcValue = (FuncValue)val;
            methodSymbol = funcValue.getScope();
            fs = new FunctionSpace("lambda", methodSymbol.getFunctionNode());
        }
        functionNode = fs.getFunctionNode();

        // prepare args in currentSpace
        for (int i = 0; i < functionNode.getParams().size(); i++) {
            NameNode nameNode = functionNode.getParams().get(i);
            if (i < callNode.getParams().size()) {
                HeteroAST paramNode = callNode.getParams().get(i);
                Value val = visit(paramNode);
                fs.put(nameNode.getToken().text, val);
                if (val instanceof FuncValue) {
                    // 动态绑定为function
                    methodSymbol.define(nameNode.getToken().text,
                        symbolTable.getSymbolWithName(paramNode.getToken().text));
                }
            }
        }

        // 设置currentScope
        Scope previousScope = symbolTable.getCurrentScope();
        symbolTable.setCurrentScope(methodSymbol);

        // 设置currentSpace
        stack.add(fs);

        // call
        Value result = visit(functionNode.getBody());

        // pop currentSpace
        stack.remove(fs);

        // pop currentScope
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

    private Value LT(LTNode node) {
        Value left = node.getLeft().visit(this);
        Value right = node.getRight().visit(this);
        if (left instanceof IntValue && right instanceof IntValue) {
            return ((IntValue) left).getValue() < ((IntValue) right).getValue() ? Value.TRUE : Value.FALSE;
        }
        throw new Error("eval lt run");
    }

    private Value EQ(EQNode node) {
        Value left = node.getLeft().visit(this);
        Value right = node.getRight().visit(this);
        if (left instanceof IntValue && right instanceof IntValue) {
            return ((IntValue) left).getValue().intValue() == ((IntValue) right).getValue()
                    ? Value.TRUE : Value.FALSE;
        }
        throw new Error("eval eq run");
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


    private MemorySpace getCurrentSpaceWithName(String name) {
        if (stack.size() > 0) {
            for (int i = stack.size() - 1; i >= 0; i--) {
                MemorySpace memorySpace = stack.get(i);
                if (memorySpace.get(name) != null) {
                    return memorySpace;
                }
            }
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
        } else if (node instanceof FloatNode) {
            return FLOAT((FloatNode)node);
        } else if (node instanceof StringNode) {
            return STRING((StringNode)node);
        } else if (node instanceof AddNode) {
            return ADD((AddNode)node);
        } else if (node instanceof SubtractNode) {
            return SUBTRACT((SubtractNode)node);
        } else if (node instanceof PrintNode) {
            return PRINT((PrintNode)node);
        } else if (node instanceof SetqNode) {
            return SETQ((SetqNode) node);
        } else if (node instanceof NameNode) {
            return NAME((NameNode)node);
        } else if (node instanceof GTNode) {
            return GT((GTNode)node);
        } else if (node instanceof LTNode) {
            return LT((LTNode) node);
        } else if (node instanceof EQNode) {
            return EQ((EQNode)node);
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
