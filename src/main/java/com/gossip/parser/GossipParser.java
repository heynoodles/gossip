package com.gossip.parser;


import com.gossip.ast.AddNode;
import com.gossip.ast.HeteroAST;
import com.gossip.ast.IntNode;
import com.gossip.ast.PrintNode;
import com.gossip.lexer.GossipLexer;
import com.gossip.lexer.Lexer;
import com.gossip.lexer.Token;
import com.gossip.lexer.TokenType;
import com.gossip.visitor.PrintVisitor;

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

    public GossipParser(Lexer lexer, int k) {
        super(lexer, k);
    }

    private HeteroAST s_expr() {
        if (LT(1).type == TokenType.INT) {
            HeteroAST intNode = new IntNode(LT(1));
            match(TokenType.INT);
            return intNode;
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
            HeteroAST param  = s_expr();
            match(TokenType.PAREN_END);
            return new PrintNode(new Token(TokenType.PRINT, "print"), param);
        } else {
            throw new Error("parse element error");
        }
    }

    // 入口
    public HeteroAST parse() {
        return s_expr();
    }

    public static void main(String[] args) {
        String input = "( + (+ 1 3) 2)";
        GossipLexer lexer = new GossipLexer(input);
        GossipParser parser = new GossipParser(lexer, 2);
        HeteroAST expr = parser.s_expr();
        PrintVisitor printVisitor = new PrintVisitor();
        expr.visit(printVisitor);
    }

}
