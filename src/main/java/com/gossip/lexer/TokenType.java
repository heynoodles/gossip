package com.gossip.lexer;

/**
 * @author gaoxin.wei
 */
public class TokenType {

    public static int NAME = 2;
    public static int COMMA = 3;
    public static int PAREN_BEGIN = 4;
    public static int PAREN_END = 5;
    public static int INT = 6;
    public static int SETQ = 7;
    public static int DEFINE = 8;
    public static int COND = 9;
    public static int STRING = 10;
    public static int CONS = 11;
    public static int CAR = 12;
    public static int CDR = 13;
    public static int LET = 14;
    public static int FLOAT = 15;
    public static int LAMBDA = 16;

    public static String[] tokenNames = {
        "n/a", "<EOF>", "NAME", "COMMA", "PAREN_BEGIN", "PAREN_END", "INT",
        "SETQ", "DEFINE", "COND", "STRING_START", "CONS", "CAR", "CDR", "LET", "FLOAT",
        "LAMBDA"
    };
}
