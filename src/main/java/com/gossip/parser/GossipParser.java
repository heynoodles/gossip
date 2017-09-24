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
 * s_expr : INT | NAME | list
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
        } else if (LT(1).type == TokenType.PRINT) {
            result = print();
        } else if (LT(1).type == TokenType.SETQ) {
            result = setq();
        } else if (LT(1).type == TokenType.DEFUN) {
            defun();
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
