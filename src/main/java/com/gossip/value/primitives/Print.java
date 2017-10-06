package com.gossip.value.primitives;

import com.gossip.value.PrimFun;
import com.gossip.value.Value;

import java.util.List;

/**
 * @author gaoxin.wei
 */
public class Print extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        Value val = args.get(0);
        System.out.println(val);
        return Value.VOID;
    }
}
