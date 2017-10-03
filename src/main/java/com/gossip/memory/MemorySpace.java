package com.gossip.memory;

import com.gossip.value.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxin.wei
 */
public class MemorySpace {

    private String name;

    private Map<String, Value> members = new HashMap<String, Value>();

    private MemorySpace() {}

    public Map<String, Value> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Value> members) {
        this.members = members;
    }

    public MemorySpace(String name) {
        this.name = name;
    }

    public void put(String id, Value val) {
        members.put(id, val);
    }

    public Value get(String id) {
        return members.get(id);
    }
}
