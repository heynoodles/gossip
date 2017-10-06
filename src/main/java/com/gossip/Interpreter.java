package com.gossip;

import com.gossip.ast.HeteroAST;
import com.gossip.lexer.GossipLexer;
import com.gossip.parser.GossipParser;
import com.gossip.symtab.SymbolTable;
import com.gossip.util.GossipException;
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


    public Interpreter(String file) {
        this.fileContent = Util.readFile(file);
    }

    public Interpreter() {}

    public Value interp() throws GossipException {
        GossipLexer lexer = new GossipLexer(fileContent);
        GossipParser parser = new GossipParser(lexer, 2, symbolTable);
        HeteroAST expr = parser.parse();
        return expr.visit(new EvalVisitor(symbolTable));
    }

    public void repl() throws GossipException {
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


    public static void main(String[] args) throws GossipException {
        if (args.length > 0) {
            String fileName = args[0];
            new Interpreter(fileName)
                .interp();
        } else {
            new Interpreter().repl();
        }
    }

}
