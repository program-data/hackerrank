package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author program@globall.ru, 15.10.2019
 */
public class Node {
    public final com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node parent;
    public final int index;
    public final int value;

    // code contract:
    // new Node[]{}; === final node
    // null = incomplete node
    public com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node[] nodes;

    public Node(com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node parent, int index) {
        this.parent = parent;
        this.index = index;
        this.value = Solution.values[index - 1];
        this.nodes = null;
    }

    @Override
    public String toString() {
        return String.format("%d -> %d", index, value);
    }

    public com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node growTo(int dst) {
        // target reached
        if (index == dst)
            return this;

        // todo
        //  if(modes != null)

        int[] m = Solution.matrix[index];
        List<com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node> list = new ArrayList<>();
        for (int i : m) {
            if (parent == null || i != parent.index) {
                com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node node1 = new com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node(this, i);
                list.add(node1);
            }
        }
        nodes = list.toArray(new com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node[0]);

        // тупик
        if (nodes.length == 0)
            return null;

        // поиск вглубь
        for (com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node node : nodes) {
            com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node target = node.growTo(dst);
            if (target != null)
                return target;
        }

        return null;
    }

    private static List<com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node> getPath(int src, int dst) {
        // todo cache
        com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node root = new com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node(null, src);
        com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node target = root.growTo(dst);
        Objects.requireNonNull(target);
        ArrayList<com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node> path = new ArrayList<>();
        for (com.hackerrank.data_structures.advanced.Counting_on_a_tree.Node node = target; node != null; node = node.parent) {
            path.add(node);
        }
        return path;
    }
}
