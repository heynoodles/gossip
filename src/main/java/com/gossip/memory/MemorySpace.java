package com.gossip.memory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxin.wei
 */
public class MemorySpace {

    private String name;

    private Map<String, Object> data = new HashMap<String, Object>();

    private MemorySpace() {}

    public MemorySpace(String name) {
        this.name = name;
    }

    public void put(String id, Object val) {
        data.put(id, val);
    }

    public Object get(String id) {
        return data.get(id);
    }
}
