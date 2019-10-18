package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author michael.malevannyy@gmail.com, 18.10.2019
 */

public class Node {
    public final int n;
    public Node parent;
    public HashSet<Integer> freePeers = new HashSet<>();

    private Node(int n) {
        this.n = n;

        int[] peer = Solution.matrix[n];
        HashSet<Integer> exits = Solution.coreExitMap.get(n);
        for (int x : peer) {
            if(exits == null || !exits.contains(x))
                freePeers.add(x);
        }
    }

    // registry
    public static Node of(int n) {
        Node node = map.get(n);
        if (node == null) {
            node = new Node(n);
            map.put(n, node);
        }
        return node;
    }

    // можно заменить массивом Node[N+1]
    static HashMap<Integer, Node> map = new HashMap<>();

    void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        // return String.valueOf(n);
        return String.format("%d -> %s", n, parent!=null ? parent.n : "null");
    }
}
