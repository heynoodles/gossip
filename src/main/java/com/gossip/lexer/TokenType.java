package com.gossip.lexer;

/**
 * @author gaoxin.wei
 */
public class TokenType {

    public static int NAME = 2;
    public static int COMMA = 3;
    public static int LBRACK = 4;
    public static int RBRACK = 5;
    public static int MINUS = 6;
    public static int ADD = 7;
    public static int PAREN_BEGIN = 8;
    public static int PAREN_END = 9;
    public static int INT = 10;
    public static int PRINT = 11;
    public static int SETQ = 12;
    public static int DEFUN = 13;
    public static int GT = 14;
    public static int LT = 15;
    public static int EQ = 16;
    public static int COND = 17;
    public static int STRING = 18;

    public static String[] tokenNames = {
        "n/a", "<EOF>", "NAME", "COMMA", "LBRACK", "RBRACK", "MINUS", "ADD", "PAREN_BEGIN", "PAREN_END", "INT",
            "PRINT", "SETQ", "DEFUN", "GT", "LT", "EQ", "COND", "STRING_START"
    };
}
