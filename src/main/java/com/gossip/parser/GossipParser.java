package com.gossip.parser;


import com.gossip.ast.*;
import com.gossip.ast.helper.TestAndActionNode;
import com.gossip.ast.helper.VarAndValNode;
import com.gossip.lexer.Lexer;
import com.gossip.lexer.Token;
import com.gossip.lexer.TokenType;
import com.gossip.symtab.*;
import com.gossip.util.GossipException;
import com.gossip.value.AnyType;

import java.util.ArrayList;
import java.util.List;


/**
 * @author gaoxin.wei
 * 语法如下：
 * -------------------------------------------
 * s_expr : list | atomic
 * list: '(' s_exp < s_exp > ')'
 * atomic: INT | FLOAT | STRING | NAME
 *  ------------------------------------------
 */
public class GossipParser extends Parser {

    private SymbolTable symbolTable;

    public GossipParser(Lexer lexer, int k, SymbolTable symbolTable) {
        super(lexer, k);
        this.symbolTable = symbolTable;
    }

    private HeteroAST s_expr() throws GossipException {
        if (LT(1).type == TokenType.PAREN_BEGIN) {
           return list();
        } else {
            return atomic();
        }
    }

    private HeteroAST atomic() throws GossipException {
        if (LT(1).type == TokenType.INT) {
            return _int();
        } else if (LT(1).type == TokenType.FLOAT) {
            return _float();
        } else if (LT(1).type == TokenType.STRING) {
            return _string();
        } else if (LT(1).type == TokenType.NAME) {
            return name();
        } else
            throw new GossipException("parse element error");
    }

    private HeteroAST name() {
        HeteroAST nameNode = new NameNode(LT(1));
        match(TokenType.NAME);
        return nameNode;
    }

    private HeteroAST _string() {
        HeteroAST strNode = new StringNode(LT(1));
        match(TokenType.STRING);
        return strNode;
    }

    private HeteroAST _float() {
        HeteroAST floatNode = new FloatNode(LT(1));
        match(TokenType.FLOAT);
        return floatNode;
    }

    private HeteroAST _int() {
        HeteroAST intNode = new IntNode(LT(1));
        match(TokenType.INT);
        return intNode;
    }

    private HeteroAST list() throws GossipException {
        HeteroAST result = null;

        match(TokenType.PAREN_BEGIN);
        if (LT(1).type == TokenType.SETQ) {
            result = setq();
        } else if (LT(1).type == TokenType.DEFINE) {
            define();
        } else if (LT(1).type == TokenType.COND) {
            result = cond();
        } else if (LT(1).type == TokenType.CONS) {
            result = cons();
        } else if (LT(1).type == TokenType.CAR) {
            result = car();
        } else if (LT(1).type == TokenType.CDR) {
            result = cdr();
        } else if (LT(1).type == TokenType.LET) {
            result = let();
        } else if (LT(1).type == TokenType.LAMBDA) {
            result = lambda();
        } else {
            result = call();
        }
        match(TokenType.PAREN_END);

        return result;
    }

    private HeteroAST cond() throws GossipException {
        // (cond (pred1 exec1)
        //       (pred2 exec2))
        match(TokenType.COND);
        List<TestAndActionNode> blocks = new ArrayList<TestAndActionNode>();
        while (LT(1).type == TokenType.PAREN_BEGIN) {
            match(TokenType.PAREN_BEGIN);
            HeteroAST test = s_expr();
            HeteroAST action = s_expr();
            match(TokenType.PAREN_END);
            TestAndActionNode block = new TestAndActionNode(test, action);
            blocks.add(block);
        }
        return new CondNode(new Token(TokenType.COND, "cond"), blocks);
    }

    private HeteroAST cons() throws GossipException {
        // (cons val1 val2)
        match(TokenType.CONS);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new ConsNode(new Token(TokenType.CONS, "cons"), left, right);
    }

    private HeteroAST car() throws GossipException {
        // (car cons1)
        match(TokenType.CAR);
        HeteroAST node = s_expr();
        return new CarNode(new Token(TokenType.CAR, "car"), node);
    }

    private HeteroAST cdr() throws GossipException {
        // (cdr cons1)
        match(TokenType.CDR);
        HeteroAST node = s_expr();
        return new CdrNode(new Token(TokenType.CDR, "cdr"), node);
    }

    private HeteroAST call() throws GossipException {
        // Token root = LT(1);
        HeteroAST operator = s_expr();
        // consume();
        // (fun p1 p2 ...)
        List<HeteroAST> params = new ArrayList<HeteroAST>();
        while (LT(1).type != TokenType.PAREN_END) {
            params.add(s_expr());
        }
        return new CallNode(operator, params);
    }

    private HeteroAST lambda() throws GossipException {
        // (lambda (...params) body)
        match(TokenType.LAMBDA);
        match(TokenType.PAREN_BEGIN);
        List<NameNode> params = new ArrayList<NameNode>();
        // parse params
        while (LT(1).type != TokenType.PAREN_END) {
            NameNode name = (NameNode) s_expr();
            params.add(name);
        }
        match(TokenType.PAREN_END);

        HeteroAST body = s_expr();

        FunctionNode functionNode = new FunctionNode(
            new Token(TokenType.DEFINE, "define"),
            new NameNode(new Token(TokenType.LAMBDA, "lambda")),
            params,
            body
        );

        return functionNode;
    }

    private HeteroAST let() throws GossipException {
        // (let binder body)
        match(TokenType.LET);

        // parse  binder: ((var val))
        List<VarAndValNode> params = new ArrayList<VarAndValNode>();
        match(TokenType.PAREN_BEGIN);
        while (LT(1).type != TokenType.PAREN_END) {
            match(TokenType.PAREN_BEGIN);
            NameNode var = (NameNode) s_expr();
            HeteroAST val = s_expr();
            params.add(new VarAndValNode(var, val));
            match(TokenType.PAREN_END);
        }
        match(TokenType.PAREN_END);

        // parse body
        HeteroAST body = list();

        return new LetNode(new Token(TokenType.LET, "let"), params, body);
    }

    private HeteroAST define() throws GossipException {
        match(TokenType.DEFINE);

        Scope previousScope = symbolTable.getCurrentScope();
        NameNode funName = null;
        MethodSymbol methodSymbol = null;
        HeteroAST body = null;
        FunctionNode functionNode = null;
        List<NameNode> params = new ArrayList<NameNode>();

        if (LT(1).type == TokenType.PAREN_BEGIN) {
            // case 1: (define (funName ...params) body)
            match(TokenType.PAREN_BEGIN);
            funName = (NameNode) s_expr();

            // build scope
            methodSymbol = new MethodSymbol(funName.getToken().text, previousScope);
            previousScope.define(funName.getToken().text, methodSymbol);
            symbolTable.setCurrentScope(methodSymbol);

            // parse params
            while (LT(1).type != TokenType.PAREN_END) {
                NameNode name = (NameNode) s_expr();
                params.add(name);
            }
            match(TokenType.PAREN_END);
            // parse body
            body = s_expr();

            functionNode = new FunctionNode(
                new Token(TokenType.DEFINE, "define"),
                funName,
                params,
                body
            );
        } else {
            // case 2: (define funName (lambda (...params) body)) or returns a functionNode
            funName = (NameNode) s_expr();

            // build scope
            methodSymbol = new MethodSymbol(funName.getToken().text, previousScope);
            previousScope.define(funName.getToken().text, methodSymbol);
            symbolTable.setCurrentScope(methodSymbol);

            functionNode = (FunctionNode) s_expr();
        }

        methodSymbol.setFunctionNode(functionNode);
        symbolTable.setCurrentScope(previousScope);

        return null;
    }

    private HeteroAST setq() throws GossipException {
        match(TokenType.SETQ);
        NameNode name = (NameNode) s_expr();
        symbolTable.getCurrentScope().define(name.getToken().text, new AnyType());
        HeteroAST valNode = s_expr();
        return new SetqNode(new Token(TokenType.SETQ, "setq"), name, valNode);
    }


    // 入口
    public HeteroAST parse() throws GossipException {
        MainNode mainNode = new MainNode();
        while (LT(1) != null && !LT(1).equals(Token.EOF)) {
            HeteroAST heteroAST = s_expr();
            if (heteroAST != null) {
                mainNode.addChild(heteroAST);
            }
        }
        return mainNode;
    }

}
