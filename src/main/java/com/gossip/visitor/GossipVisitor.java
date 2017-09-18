package com.gossip.visitor;

import com.gossip.ast.HeteroAST;

/**
 * @author gaoxin.wei
 */
public interface GossipVisitor {

    Object visit(HeteroAST node);
}
