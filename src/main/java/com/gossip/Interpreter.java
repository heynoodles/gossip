package com.gossip;

import com.gossip.ast.HeteroAST;
import com.gossip.lexer.GossipLexer;
import com.gossip.memory.MemorySpace;
import com.gossip.parser.GossipParser;
import com.gossip.symtab.SymbolTable;
import com.gossip.util.Util;
import com.gossip.value.Value;
import com.gossip.visitor.EvalVisitor;

import java.util.Scanner;

/**
 * @author gaoxin.wei
 */
public class Interpreter {

    private String fileContent;

    private SymbolTable symbolTable = new SymbolTable();

    private MemorySpace memorySpace = new MemorySpace("global");

    public Interpreter(String file) {
        this.fileContent = Util.readFile(file);
    }

    public Interpreter() {}

    public Value interp() {
        GossipLexer lexer = new GossipLexer(fileContent);
        GossipParser parser = new GossipParser(lexer, 2, symbolTable);
        HeteroAST expr = parser.parse();
        return expr.visit(new EvalVisitor(symbolTable, memorySpace));
    }

    public void repl() {
        System.out.println("Welcome to Gossip REPL v1.0");
        System.out.println("see: https://github.com/heynoodles/gossip");
        System.out.println("");
        System.out.println("Type in expressions for evaluation. Or :quit for quit.");
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        System.out.print("gossip> ");
        while (scanner.hasNextLine()) {
            String code = scanner.nextLine();
            if (":quit".equalsIgnoreCase(code)) {
                return;
            } else if (code.startsWith(":load")) {
                String[] split = code.split("\\s+");
                String fine = split[1];
                this.fileContent = Util.readFile(fine);
            } else {
                this.fileContent = code;
            }
            System.out.println(interp());
            System.out.print("gossip> ");
        }
    }

    public static void main(String[] args) {

        if (args.length > 0) {
            String fileName = args[0];
            new Interpreter(fileName)
                .interp();
        } else {
            new Interpreter().repl();
        }

//        new Interpreter("tests/let.gossip")
//            .interp();

    }

}
