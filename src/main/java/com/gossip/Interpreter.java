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

    private String file;

    public Interpreter(String file) {
        this.file = Util.readFile(file);
    }

    public void interp() {
        SymbolTable symbolTable = new SymbolTable();
        MemorySpace memorySpace = new MemorySpace("global");
        GossipLexer lexer = new GossipLexer(file);
        GossipParser parser = new GossipParser(lexer, 2, symbolTable);
        HeteroAST expr = parser.parse();
        expr.visit(new EvalVisitor(symbolTable, memorySpace));
    }

    public static void main(String[] args) {
        new Interpreter("tests/test2.gossip")
            .interp();
    }

}
