package com.gossip.lexer;

/**
 * @author gaoxin.wei
 */
public class TokenType {

    public static int NAME = 2;
    public static int PAREN_BEGIN = 3;
    public static int PAREN_END = 4;
    public static int INT = 5;
    public static int SETQ = 6;
    public static int DEFINE = 7;
    public static int COND = 8;
    public static int STRING = 9;
    public static int LET = 10;
    public static int FLOAT = 11;
    public static int LAMBDA = 12;

    public static String[] tokenNames = {
        "n/a", "<EOF>", "NAME", "PAREN_BEGIN", "PAREN_END", "INT",
        "SETQ", "DEFINE", "COND", "STRING_START", "LET", "FLOAT",
        "LAMBDA"
    };
}
