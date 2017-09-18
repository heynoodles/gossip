package com.gossip.lexer;

/**
 * @author gaoxin.wei
 */
public class TokenType {

    public static int NAME = 2;
    public static int COMMA = 3;
    public static int LBRACK = 4;
    public static int RBRACK = 5;
    public static int EQUALS = 6;
    public static int ADD = 7;
    public static int PAREN_BEGIN = 8;
    public static int PAREN_END = 9;
    public static int INT = 10;
    public static int PRINT = 11;


    public static String[] tokenNames = {
        "n/a", "<EOF>", "NAME", "COMMA", "LBRACK", "RBRACK", "EQUALS", "ADD", "PAREN_BEGIN", "PAREN_END", "INT",
            "PRINT"
    };
}
