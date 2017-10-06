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
                case '(':
                    consume();
                    return new Token(TokenType.PAREN_BEGIN, "(");
                case ')':
                    consume();
                    return new Token(TokenType.PAREN_END, ")");
                default:
                    if (isNumber()) {
                        return NUMBER();
                    } else if (isString()) {
                        return STRING();
                    } else if (isSetq()) {
                        return SETQ();
                    } else if (isCond()) {
                        return COND();
                    } else if (isDefine()) {
                        return DEFINE();
                    } else if (isLet()) {
                        return LET();
                    } else if (isLambda()) {
                        return LAMBDA();
                    } else {
                        return NAME();
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
        return safeMatch("cond");
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
        return safeMatch("setq");
    }

    private boolean isNumber() {
        StringBuilder buf = new StringBuilder();
        int q = p;
        for (char val = c; isDigit(val); val = lookahead(++q)) {
            buf.append(val);
        }
        return isIntValue(buf.toString()) || isFloatValue(buf.toString());
    }

    private Token NUMBER() {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (isDigit());
        if (isIntValue(buf.toString())) {
            return new Token(TokenType.INT, buf.toString());
        } else if (isFloatValue(buf.toString())) {
            return new Token(TokenType.FLOAT, buf.toString());
        } else {
            throw new RuntimeException("not a number");
        }
    }

    private boolean isFloatValue(String value) {
        try {
            Double val = Double.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isIntValue(String value) {
        try {
            Integer val = Integer.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isDigit() {
        return isDigit(c);
    }

    private boolean isDigit(char val) {
        return Character.isDigit(val) || val == '.' || val == '+' || val == '-';
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

    private boolean skipComments() {
        boolean found = false;

        if (safeMatch("--")) {
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


    private boolean isLETTER() {
        return Character.isLetterOrDigit(c) || c == '=';
    }

    private boolean isDefine() {
        return safeMatch("define");
    }

    private Token DEFINE() {
        for (char c : "define".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.DEFINE, "define");
    }

    private boolean isLet() {
        return safeMatch("let");
    }

    private Token LET() {
        for (char c : "let".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.LET, "let");
    }


    private boolean isLambda() {
        return safeMatch("lambda");
    }

    private Token LAMBDA() {
        for (char c : "lambda".toCharArray()) {
            match(c);
        }
        return new Token(TokenType.LAMBDA, "lambda");
    }

}
