package com.gossip;

import com.gossip.ast.HeteroAST;
import com.gossip.lexer.GossipLexer;
import com.gossip.memory.MemorySpace;
import com.gossip.parser.GossipParser;
import com.gossip.symtab.SymbolTable;
import com.gossip.visitor.EvalVisitor;

/**
 * @author gaoxin.wei
 */
public class Interpreter {

    public static void main(String[] args) {
        String input = Util.readFile("tests/test1.gossip");
        SymbolTable symbolTable = new SymbolTable();
        MemorySpace memorySpace = new MemorySpace("global");
        GossipLexer lexer = new GossipLexer(input);
        GossipParser parser = new GossipParser(lexer, 2, symbolTable);
        HeteroAST expr = parser.parse();
        expr.visit(new EvalVisitor(symbolTable, memorySpace));
    }

}
