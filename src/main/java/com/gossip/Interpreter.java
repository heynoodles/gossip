package com.gossip;

import com.gossip.ast.HeteroAST;
import com.gossip.lexer.GossipLexer;
import com.gossip.parser.GossipParser;
import com.gossip.visitor.EvalVisitor;

/**
 * @author gaoxin.wei
 */
public class Interpreter {

    public static void main(String[] args) {
        String input = "(print ( + (+ 11 3) 2))";
        GossipLexer lexer = new GossipLexer(input);
        GossipParser parser = new GossipParser(lexer, 2);
        HeteroAST expr = parser.parse();
        expr.visit(new EvalVisitor());
    }

}
