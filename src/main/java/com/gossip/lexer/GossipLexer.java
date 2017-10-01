package com.gossip.lexer;


/**
 * @author gaoxin.wei
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
                case '(':
                    consume();
                    return new Token(TokenType.PAREN_BEGIN, "(");
                case ')':
                    consume();
                    return new Token(TokenType.PAREN_END, ")");
                case '+':
                    consume();
                    return new Token(TokenType.ADD, "+");
                case '-':
                    consume();
                    return new Token(TokenType.MINUS, "-");
                case '>':
                    consume();
                    return new Token(TokenType.GT, ">");
                case '<':
                    consume();
                    return new Token(TokenType.LT, "<");
                case '=':
                    consume();
                    return new Token(TokenType.EQ, "=");
                default:
                    if (isDigit()) {
                        return INT();
                    } else if (isString()) {
                        return STRING();
                    } else if (isCons()) {
                        return CONS();
                    } else if (isPrint()) {
                        return PRINT();
                    } else if (isCar()) {
                        return CAR();
                    } else if (isCdr()) {
                        return CDR();
                    } else if (isSetq()) {
                        return SETQ();
                    } else if (isCond()) {
                        return COND();
                    } else if (isDefine()) {
                        return DEFINE();
                    } else if (isLETTER()) {
                        return NAME();
                    } else {
                        throw new Error("invalid character: " + c);
                    }
            }
        }
        return new Token(EOF_TYPE, "<EOF>");
    }

    private boolean isString() {
        return c == '"';
    }

    private Token STRING() {
        match('"');
        StringBuilder buf = new StringBuilder();
        while (c != '"') {
            buf.append(c);
            consume();
        }
        match('"');
        return new Token(TokenType.STRING, buf.toString());
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

    private boolean isCar() {
        return lookAhead("car");
    }

    private boolean isCdr() {
        return lookAhead("cdr");
    }

    private boolean isCons() {
        return lookAhead("cons");
    }

    private Token PRINT() {
        for (char c : "print".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.PRINT, "print");
    }

    private Token CAR() {
        for (char c : "car".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.CAR, "car");
    }

    private Token CDR() {
        for (char c : "cdr".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.CDR, "cdr");
    }

    private Token CONS() {
        for (char c : "cons".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.CONS, "cons");
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

    boolean isLetterOrDigit() {
        return isLETTER() || isDigit();
    }

    boolean isLETTER() {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDefine() {
        return lookAhead("define");
    }

    private Token DEFINE() {
        for (char c : "define".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.DEFINE, "define");
    }

}
