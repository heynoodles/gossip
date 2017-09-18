package com.gossip.lexer;

/**
 * @author gaoxin.wei
 */
public abstract class Lexer {

    public static final char EOF = (char)-1; // EOF 文件结尾

    public static final int EOF_TYPE = 1; // 表示EOF词法类型

    String input; // 输入字符串

    int p = 0; // 当前输入字符的下标

    char c; // 当前字符

    public Lexer(String input) {
        this.input = input;
        c = input.charAt(p);
    }

    public void consume() {
        p++;
        if (p >= input.length()) {
            c = EOF;
        } else {
            c = input.charAt(p);
        }
    }

    public void match(char x) {
        if (c == x) {
            consume();
        } else {
            throw new Error("expecting " + x + "; found " + c);
        }
    }

    public abstract Token nextToken();

    public abstract String getTokenName(int tokenType);
}
