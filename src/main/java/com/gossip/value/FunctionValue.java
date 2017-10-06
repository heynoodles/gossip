package com.gossip.value;

import com.gossip.ast.FunctionNode;

/**
 * @author gaoxin.wei
 * 只包含方法模板，不包含实际调用的任何信息
 */
public class FunctionValue extends Value<FunctionNode> {

    public FunctionValue(FunctionNode functionNode) {
        this.value = functionNode;
    }

    @Override
    public Value create(FunctionNode value) {
        return null;
    }
}
