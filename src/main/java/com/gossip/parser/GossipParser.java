package com.gossip.parser;


import com.gossip.ast.*;
import com.gossip.lexer.Lexer;
import com.gossip.lexer.Token;
import com.gossip.lexer.TokenType;
import com.gossip.symtab.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author gaoxin.wei
 * 语法如下：
 * -------------------------------------------
 * s_expr : INT | STRING | NAME | list
 * list: '(' s_exp < s_exp > ')'
 *  ------------------------------------------
 */
public class GossipParser extends Parser {

    private SymbolTable symbolTable;

    public GossipParser(Lexer lexer, int k, SymbolTable symbolTable) {
        super(lexer, k);
        this.symbolTable = symbolTable;
    }

    private HeteroAST s_expr() {
        if (LT(1).type == TokenType.INT) {
            HeteroAST intNode = new IntNode(LT(1));
            match(TokenType.INT);
            return intNode;
        } else if (LT(1).type == TokenType.STRING) {
            HeteroAST strNode = new StringNode(LT(1));
            match(TokenType.STRING);
            return strNode;
        } else if (LT(1).type == TokenType.NAME) {
            // 变量
            HeteroAST nameNode = new NameNode(LT(1));
            symbolTable.getCurrentScope().define(new VariableSymbol(nameNode.getToken().text));
            match(TokenType.NAME);
            return nameNode;
        } else if (LT(1).type == TokenType.PAREN_BEGIN) {
           return list();
        } else
            throw new Error("parse element error");
    }

    private HeteroAST list() {
        HeteroAST result = null;

        match(TokenType.PAREN_BEGIN);
        if (LT(1).type == TokenType.ADD) {
            result = add();
        } else if (LT(1).type == TokenType.MINUS) {
            result = minus();
        } else if (LT(1).type == TokenType.PRINT) {
            result = print();
        } else if (LT(1).type == TokenType.SETQ) {
            result = setq();
        } else if (LT(1).type == TokenType.DEFUN) {
            defun();
        } else if (LT(1).type == TokenType.GT) {
            result = gt();
        } else if (LT(1).type == TokenType.LT) {
            result = lt();
        } else if (LT(1).type == TokenType.EQ) {
            result = eq();
        } else if (LT(1).type == TokenType.COND) {
            result = cond();
        } else if (LT(1).type == TokenType.CONS) {
            result = cons();
        } else if (LT(1).type == TokenType.CAR) {
            result = car();
        } else if (LT(1).type == TokenType.CDR) {
            result = cdr();
        } else if (LT(1).type == TokenType.NAME) {
            Symbol symbol = symbolTable.getSymbolWithName(LT(1).text);
            if (symbol != null && symbol instanceof MethodSymbol) {
                result = call();
            }
        } else {
            throw new Error("parse element error");
        }
        match(TokenType.PAREN_END);

        return result;
    }

    private HeteroAST lt() {
        match(TokenType.LT);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new LTNode(new Token(TokenType.LT, "<"), left, right);
    }

    private HeteroAST eq() {
        match(TokenType.EQ);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new EQNode(new Token(TokenType.EQ, "="), left, right);
    }

    private HeteroAST cond() {
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

    private HeteroAST cons() {
        // (cons val1 val2)
        match(TokenType.CONS);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new ConsNode(new Token(TokenType.CONS, "cons"), left, right);
    }

    private HeteroAST car() {
        // (car cons1)
        match(TokenType.CAR);
        HeteroAST node = s_expr();
        return new CarNode(new Token(TokenType.CAR, "car"), node);
    }

    private HeteroAST cdr() {
        // (cdr cons1)
        match(TokenType.CDR);
        HeteroAST node = s_expr();
        return new CdrNode(new Token(TokenType.CDR, "cdr"), node);
    }

    private HeteroAST gt() {
        match(TokenType.GT);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new GTNode(new Token(TokenType.GT, ">"), left, right);
    }

    private HeteroAST call() {
        Token root = LT(1);
        consume();
        // (fun p1 p2 ...)
        List<HeteroAST> params = new ArrayList<HeteroAST>();
        while (LT(1).type != TokenType.PAREN_END) {
            params.add(s_expr());
        }
        return new CallNode(root, params);
    }

    private HeteroAST defun() {
        // (defun funName (...params) body)
        match(TokenType.DEFUN);

        // parse funName
        NameNode funName = (NameNode)s_expr();

        // build scope
        Scope previousScope = symbolTable.getCurrentScope();
        MethodSymbol methodSymbol = new MethodSymbol(funName.getToken().text, previousScope);
        previousScope.define(methodSymbol);
        symbolTable.setCurrentScope(methodSymbol);

        // parse params
        List<NameNode> params = new ArrayList<NameNode>();
        match(TokenType.PAREN_BEGIN);
        while (LT(1).type != TokenType.PAREN_END) {
            params.add((NameNode) s_expr());
        }
        match(TokenType.PAREN_END);

        // parse body
        HeteroAST body = s_expr();

        FunctionNode functionNode = new FunctionNode(
            new Token(TokenType.DEFUN, "defun"),
            funName,
            params,
            body
        );

        methodSymbol.setFunctionNode(functionNode);
        symbolTable.setCurrentScope(previousScope);

        return null;
    }

    private HeteroAST setq() {
        match(TokenType.SETQ);
        NameNode name = (NameNode) s_expr();
        HeteroAST valNode = s_expr();
        return new SetqNode(new Token(TokenType.SETQ, "setq"), name, valNode);
    }

    private HeteroAST print() {
        match(TokenType.PRINT);
        HeteroAST param = s_expr();
        return new PrintNode(new Token(TokenType.PRINT, "print"), param);
    }

    private HeteroAST add() {
        match(TokenType.ADD);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new AddNode(new Token(TokenType.ADD, "+"), left, right);
    }

    private HeteroAST minus() {
        match(TokenType.MINUS);
        HeteroAST left = s_expr();
        HeteroAST right = s_expr();
        return new MinusNode(new Token(TokenType.MINUS, "-"), left, right);
    }

    // 入口
    public HeteroAST parse() {
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
