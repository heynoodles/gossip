package com.gossip.lexer;

import static com.gossip.lexer.Lexer.EOF_TYPE;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;

        Token token = (Token) o;

        return type == token.type;

    }

    @Override
    public int hashCode() {
        return type;
    }

    public final static Token EOF = new Token(EOF_TYPE, "<EOF>");
}
