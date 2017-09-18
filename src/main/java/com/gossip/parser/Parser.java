package com.gossip.parser;

import com.gossip.lexer.Lexer;
import com.gossip.lexer.Token;

/**
 * @author gaoxin.wei
 * LL(K)语法解析器
 */
public abstract class Parser {

    protected Lexer input;
    protected int k; // 表示缓存个数
    protected int p = 0; // 环形缓冲区中下一个token的位置
    protected Token[] lookahead; // 环形缓冲区

    public Parser(Lexer lexer, int k) {
        this.input = lexer;
        this.k = k;
        lookahead = new Token[k];
        for (int i = 0; i < k; i++) {
            lookahead[i] = input.nextToken();
        }
    }

    protected Token LT(int i) {
        return lookahead[(p +i -1) % k];
    }

    protected int LI(int i) {
        return LT(i).type;
    }

    protected void consume() {
        lookahead[p] = input.nextToken();
        p = (p + 1) % k;
    }

    protected void match(int x) {
        if (LT(1).type == x) {
            consume();
            return;
        }
        throw new Error("match error");
    }
}
