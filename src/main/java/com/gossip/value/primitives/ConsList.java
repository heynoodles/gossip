package com.gossip.value.primitives;

import com.gossip.value.ConsValue;
import com.gossip.value.PrimFun;
import com.gossip.value.Value;
import com.gossip.value.cons.ConsInner;

import java.util.List;

/**
 * Created by gaoxinwei on 2017/10/11.
 */
public class ConsList extends PrimFun {

    @Override
    public Value apply(List<Value> args) {
        ConsValue result = null;
        ConsValue pre = null;
        for (Value value : args) {
            ConsValue cur = new ConsValue(new ConsInner());
            if (result == null) {
                result = cur;
            }
            cur.getValue().setLeft(value);
            if (pre != null) {
                pre.getValue().setRight(cur);
            }
            pre = cur;
        }
        return result;
    }

    @Override
    public Value create(Object value) {
        return LIST;
    }
}
