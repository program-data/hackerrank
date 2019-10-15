package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
public class Solution {
    // Complete the solve function below.

    // tree === array[n-1][2]
    private static int[][] buildCompressedMatrix(int[][] tree) {
        int n = tree.length + 1;
        TreeMap<Integer, TreeSet<Integer>> map = new TreeMap<>();
        for (int i = 0; i < tree.length; ++i) {
            int u = tree[i][0];
            int v = tree[i][1];
            map.computeIfAbsent(u, k -> new TreeSet<>()).add(v);
            map.computeIfAbsent(v, k -> new TreeSet<>()).add(u);
        }

        int[][] matrix = new int[n + 1][];
        for (Map.Entry<Integer, TreeSet<Integer>> entry : map.entrySet()) {
            int src = entry.getKey();
            int[] dst = entry.getValue().stream().mapToInt(value -> value).toArray();
            matrix[src] = dst;
        }

        return matrix;
    }

    static class Node {
        public final Node parent;
        public final int index;
        public final int value;

        // code contract:
        // new Node[]{}; === final node
        // null = incomplete node
        public Node[] nodes;

        public Node(Node parent, int index) {
            this.parent = parent;
            this.index = index;
            this.value = values[index - 1];
            this.nodes = null;
        }

        @Override
        public String toString() {
            return String.format("%d -> %d", index, value);
        }

        public Node growTo(int dst) {
            // target reached
            if (index == dst)
                return this;

            // todo
            //  if(modes != null)

            int[] m = Solution.matrix[index];
            List<Node> list = new ArrayList<>();
            for (int i : m) {
                if (parent == null || i != parent.index) {
                    Node node1 = new Node(this, i);
                    list.add(node1);
                }
            }
            nodes = list.toArray(new Node[0]);

            // тупик
            if (nodes.length == 0)
                return null;

            // поиск вглубь
            for (Node node : nodes) {
                Node target = node.growTo(dst);
                if (target != null)
                    return target;
            }

            return null;
        }

        private static List<Node> getPath(int src, int dst) {
            // todo cache
            Node root = new Node(null, src);
            Node target = root.growTo(dst);
            Objects.requireNonNull(target);
            ArrayList<Node> path = new ArrayList<>();
            for (Node node = target; node != null; node = node.parent) {
                path.add(node);
            }
            return path;
        }
    }

    private static int solve(int[] query) {
        List<Node> path1 = Node.getPath(query[0], query[1]);
        List<Node> path2 = Node.getPath(query[2], query[3]);
        path1.sort(Comparator.comparingInt(o -> o.value));
        path2.sort(Comparator.comparingInt(o -> o.value));

        int k=0;

        // int size1 = path1.size();
        // int size2 = path2.size();
        // for (int i = 0, j = 0; i < size1 && j < size2; /* BEWARE */) {
        //     Node ni = path1.get(i);
        //     Node nj = path1.get(j);
        //     if(ni.value == nj.value) {
        //         if(ni.index != nj.index)
        //             ++k;
        //     }
        //
        // }


        for (Node node1 : path1) {
            for (Node node2 : path2) {
                if(node1.index != node2.index && node1.value == node2.value)
                    ++k;
            }
        }
        return k;
    }

    // compressed direction matrix
    private static int[][] matrix;
    private static int[] values;

    // queries === array[k][4]
    static int[] solve(int[] values, int[][] tree, int[][] queries) {
        matrix = buildCompressedMatrix(tree);
        Solution.values = values;

        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            result[i] = solve(queries[i]);
        }

        return result;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nq = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nq[0]);

        int q = Integer.parseInt(nq[1]);

        int[] c = new int[n];

        String[] cItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int cItr = 0; cItr < n; cItr++) {
            int cItem = Integer.parseInt(cItems[cItr]);
            c[cItr] = cItem;
        }

        int[][] tree = new int[n - 1][2];

        for (int treeRowItr = 0; treeRowItr < n - 1; treeRowItr++) {
            String[] treeRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int treeColumnItr = 0; treeColumnItr < 2; treeColumnItr++) {
                int treeItem = Integer.parseInt(treeRowItems[treeColumnItr]);
                tree[treeRowItr][treeColumnItr] = treeItem;
            }
        }

        int[][] queries = new int[q][4];

        for (int queriesRowItr = 0; queriesRowItr < q; queriesRowItr++) {
            String[] queriesRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int queriesColumnItr = 0; queriesColumnItr < 4; queriesColumnItr++) {
                int queriesItem = Integer.parseInt(queriesRowItems[queriesColumnItr]);
                queries[queriesRowItr][queriesColumnItr] = queriesItem;
            }
        }

        int[] result = solve(c, tree, queries);

        for (int resultItr = 0; resultItr < result.length; resultItr++) {
            bufferedWriter.write(String.valueOf(result[resultItr]));

            if (resultItr != result.length - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }

    public Object[] load(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path)).useLocale(Locale.US);

        String[] nq = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nq[0]);

        int q = Integer.parseInt(nq[1]);

        int[] c = new int[n];

        String[] cItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int cItr = 0; cItr < n; cItr++) {
            int cItem = Integer.parseInt(cItems[cItr]);
            c[cItr] = cItem;
        }

        int[][] tree = new int[n - 1][2];

        for (int treeRowItr = 0; treeRowItr < n - 1; treeRowItr++) {
            String[] treeRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int treeColumnItr = 0; treeColumnItr < 2; treeColumnItr++) {
                int treeItem = Integer.parseInt(treeRowItems[treeColumnItr]);
                tree[treeRowItr][treeColumnItr] = treeItem;
            }
        }

        int[][] queries = new int[q][4];

        for (int queriesRowItr = 0; queriesRowItr < q; queriesRowItr++) {
            String[] queriesRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int queriesColumnItr = 0; queriesColumnItr < 4; queriesColumnItr++) {
                int queriesItem = Integer.parseInt(queriesRowItems[queriesColumnItr]);
                queries[queriesRowItr][queriesColumnItr] = queriesItem;
            }
        }

        return new Object[]{c, tree, queries};
    }

    @Test
    public void test() throws FileNotFoundException {
        Object[] objects = load("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/input00.txt");
        int[] c = (int[]) objects[0];
        int[][] tree = (int[][]) objects[1];
        int[][] queries = (int[][]) objects[2];

        Assert.assertArrayEquals(new int[]{0, 1, 3, 2, 0}, solve(c, tree, queries));

    }
}

