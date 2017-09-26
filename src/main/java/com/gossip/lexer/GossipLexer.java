package com.gossip.lexer;


/**
 * @author gaoxin.wei
 * LL(1)词法解析器
 */
public class GossipLexer extends Lexer {


    public GossipLexer(String input) {
        super(input);
    }

    public Token nextToken() {
        while (c != EOF) {
            skipComments();
            switch (c) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    WS();
                    continue;
                case ',':
                    consume();
                    return new Token(TokenType.COMMA, ",");
                case '[':
                    consume();
                    return new Token(TokenType.LBRACK, "[");
                case ']':
                    consume();
                    return new Token(TokenType.RBRACK, "]");
                case '=':
                    consume();
                    return new Token(TokenType.EQUALS, "=");
                case '(':
                    consume();
                    return new Token(TokenType.PAREN_BEGIN, "(");
                case ')':
                    consume();
                    return new Token(TokenType.PAREN_END, ")");
                case '+':
                    consume();
                    return new Token(TokenType.ADD, "+");
                case '>':
                    consume();
                    return new Token(TokenType.GT, ">");
                default:
                    if (isDigit()) {
                        return INT();
                    } else if (isPrint()) {
                        return PRINT();
                    } else if (isSetq()) {
                        return SETQ();
                    } else if (isCond()) {
                        return COND();
                    } else if (isDefun()) {
                        return DEFUN();
                    } else if (isLETTER()) {
                        return NAME();
                    } else {
                        throw new Error("invalid character: " + c);
                    }
            }
        }
        return new Token(EOF_TYPE, "<EOF>");
    }

    private boolean isCond() {
        return lookAhead("cond");
    }

    private Token COND() {
        for (char c : "cond".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.COND, "cond");
    }

    private Token SETQ() {
        for (char c : "setq".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.SETQ, "setq");
    }

    private boolean isSetq() {
        return lookAhead("setq");
    }

    private boolean isPrint() {
        return lookAhead("print");
    }

    private Token PRINT() {
        for (char c : "print".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.PRINT, "print");
    }

    private Token INT() {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (isDigit());
        return new Token(TokenType.INT, buf.toString());
    }

    boolean isDigit() {
        return c >= '0' && c <= '9';
    }

    private Token NAME() {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (isLETTER());
        return new Token(TokenType.NAME, buf.toString());
    }

    private void WS() {
        while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            consume();
        }
    }


    public boolean skipComments() {
        boolean found = false;

        if (lookAhead("--")) {
            found = true;

            // skip to line end
            while (p < input.length() && input.charAt(p) != '\n') {
                consume();
            }
        }
        return found;
    }


    public String getTokenName(int tokenType) {
        return TokenType.tokenNames[tokenType];
    }

    boolean isLETTER() {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDefun() {
        return lookAhead("defun");
    }

    private Token DEFUN() {
        for (char c : "defun".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.DEFUN, "defun");
    }

}
