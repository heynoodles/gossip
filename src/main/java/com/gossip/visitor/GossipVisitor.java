package com.gossip.visitor;

import com.gossip.ast.HeteroAST;
import com.gossip.value.Value;

/**
 * @author gaoxin.wei
 */
public interface GossipVisitor {

    Value visit(HeteroAST node);
}
