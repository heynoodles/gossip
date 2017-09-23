package com.gossip.parser;


import com.gossip.ast.*;
import com.gossip.lexer.Lexer;
import com.gossip.lexer.Token;
import com.gossip.lexer.TokenType;
import com.gossip.symtab.SymbolTable;
import com.gossip.symtab.VariableSymbol;

/**
 * @author gaoxin.wei
 * 语法如下：
 * -------------------------------------------
 * s_expr : list
 *          | INT
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
            symbolTable.define(new VariableSymbol(nameNode.getToken().text, null));
            match(TokenType.NAME);
            return nameNode;
        } else if (LT(1).type == TokenType.PAREN_BEGIN) {
           return list();
        } else
            throw new Error("parse element error");
    }

    private HeteroAST list() {
        match(TokenType.PAREN_BEGIN);
        if (LT(1).type == TokenType.ADD) {
            match(TokenType.ADD);
            HeteroAST left = s_expr();
            HeteroAST right = s_expr();
            match(TokenType.PAREN_END);
            return new AddNode(new Token(TokenType.ADD, "+"), left, right);
        } else if (LT(1).type == TokenType.PRINT) {
            match(TokenType.PRINT);
            HeteroAST param = s_expr();
            match(TokenType.PAREN_END);
            return new PrintNode(new Token(TokenType.PRINT, "print"), param);
        } else if (LT(1).type == TokenType.SETQ) {
            match(TokenType.SETQ);
            NameNode name = (NameNode) s_expr();
            HeteroAST valNode = s_expr();
            match(TokenType.PAREN_END);
            return new SetqNode(new Token(TokenType.SETQ, "setq"), name, valNode);
        } else {
            throw new Error("parse element error");
        }
    }

    // 入口
    public HeteroAST parse() {
        MainNode mainNode = new MainNode();
        while (LT(1) != null && !LT(1).equals(Token.EOF)) {
            mainNode.addChild(s_expr());
        }
        return mainNode;
    }

}
