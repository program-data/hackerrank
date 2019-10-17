package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author michael.malevannyy@gmail.com, 18.10.2019
 */

public class Node {
    public final int n;
    public final int[] peer;
    public Node parent;
    public int free;
    public HashSet<Integer> freePeers = new HashSet<>();

    private Node(int n) {
        this.n = n;
        this.peer = Solution.matrix[n];
        for (int x : peer) {
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
    private static HashMap<Integer, Node> map = new HashMap<>();
}
