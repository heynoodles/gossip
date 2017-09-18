package com.gossip.lexer;

/**
 * @author gaoxin.wei
 */
public class Token {

    public int type;

    public String text;

    public Token(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public String toString() {
        String tname = TokenType.tokenNames[type];
        return "<'" + text + "', " + tname + ">";
    }
}
