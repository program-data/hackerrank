package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public class Solution {
    // Complete the solve function below.
    // tree === array[n-1][2]
    // private static int[][] buildCompressedMatrix(int[][] tree) {
    //     // long t0 = System.nanoTime();
    //     int n = tree.length + 1;
    //     TreeMap<Integer, TreeSet<Integer>> map = new TreeMap<>();
    //     for (int i = 0; i < tree.length; ++i) {
    //         int u = tree[i][0];
    //         int v = tree[i][1];
    //         map.computeIfAbsent(u, k -> new TreeSet<>()).add(v);
    //         map.computeIfAbsent(v, k -> new TreeSet<>()).add(u);
    //     }
    //
    //     int[][] matrix = new int[n + 1][];
    //     for (Map.Entry<Integer, TreeSet<Integer>> entry : map.entrySet()) {
    //         int src = entry.getKey();
    //         int[] dst = entry.getValue().stream().mapToInt(value -> value).toArray();
    //         matrix[src] = dst;
    //     }
    //
    //     // special unused case, init it for safety only
    //     matrix[0] = new int[]{};
    //
    //     // long t1 = System.nanoTime();  System.err.printf("%.3f\n",(t1-t0)*0.000_000_001); // ~0.1c on test #4
    //     return matrix;
    // }

    // 3 пиешм хвосты в карту на основе индексов массива: элементв хвоста -> узел ядра
    // private static void writeTailToMap(int n) {
    //     // потенцияльно это можно заменить огромным переиспользуемым буфером потом скопировать только заполненную часть, может оказаться быстрее
    //     List<Integer> list = new ArrayList<>(12);
    //     for (int[] peer = Solution.matrix[n]; peer.length < 3; n = !list.contains(peer[0]) ? peer[0] : peer[1], peer = Solution.matrix[n]) {
    //         list.add(n);
    //     }
    //     // sux
    //     int[] tail = list.stream().mapToInt(i -> i).toArray();
    //
    //     // 3 пиешм хвосты в карту хвост -> ядро
    //     for (int t : tail) {
    //         // индетивицируем хвост его краем.
    //         tailIdentifier[t] = tail[0];
    //         // и это тоже
    //         tailCore[t] = n;
    //         // и так
    //         corePerimeter.add(n);
    //         // и еще карту вылетов
    //
    //         int e = tail[tail.length - 1];
    //         HashSet<Integer> exists = exitMap.putIfAbsent(n, new HashSet<>(Collections.singletonList(e)));
    //         if (exists != null)
    //             exists.add(e);
    //     }
    //
    //     // пишем весь хвост в его порядке в крту хвостов
    //     tailMap.put(tail[0], tail);
    // }

    // tests ok
    private static int count(int[] path1, int[] path2) {
        int k = 0;

        int[][] list1 = Arrays.stream(path1).mapToObj(i -> new int[]{values[i - 1], i}).sorted(Comparator.comparingInt((int[] o) -> o[0]).thenComparingInt(o -> o[1])).collect(Collectors.toList()).toArray(new int[][]{});
        int[][] list2 = Arrays.stream(path2).mapToObj(i -> new int[]{values[i - 1], i}).sorted(Comparator.comparingInt((int[] o) -> o[0]).thenComparingInt(o -> o[1])).collect(Collectors.toList()).toArray(new int[][]{});

        for (int i = 0, j = 0, size1 = list1.length, size2 = list2.length; i < size1 && j < size2; /*BEWARE*/) {
            int[] ni = list1[i];
            int[] nj = list2[j];
            if (ni[0] < nj[0]) {
                ++i;
            }
            else if (ni[0] > nj[0]) {
                ++j;
            }
            else {
                int si = i; // стартовый индекс в большом массиве
                for (; i < size1 && list1[i][0] == ni[0]; ++i) ;

                int sj = j; // стартовый индекс в большом массиве
                for (; j < size2 && list2[j][0] == nj[0]; ++j) ;

                // размер одинакоывых >=1 т.к как минимум один точно будет = текущий
                // дале есть смысл идти только если кто-то ширше 1 символа
                int di = i - si;
                int dj = j - sj;

                int q = 0;
                // ищщем количество одинаковых индексов и указаных поддиапазонах и вычитаем его из произведения разницы
                for (int ii = si, ij = sj; ii < i && ij < j; /* BEWARE*/) {
                    if (list1[ii][1] < list2[ij][1]) {
                        ++ii;
                    }
                    else if (list1[ii][1] > list2[ij][1]) {
                        ++ij;
                    }
                    else {
                        ++q;
                        ++ii;
                        ++ij;
                    }
                }

                k += di * dj - q;
            }
        }

        return k;
    }

    // task size
    private static int N;
    // values
    private static int[] values;
    // комутатор, *хвост -> иднетификатор хвоста  (по его кончику)
    private static int[] tailIdentifier;
    // коммутатор *хвост -> ядро сети
    private static int[] tailCore;
    // карта хвостов
    private static HashMap<Integer, int[]> tailMap = new HashMap<>();
    // периметр ядра (узлы держатели хвостов)
    private static HashSet<Integer> corePerimeter = new HashSet<>();

    // update: tree given ordered [parent -> slave]
    static int[] solve(int[] values, int[][] tree, int[][] queries) {
        Solution.N = values.length;
        Solution.values = values;

        buildTree(tree);

        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int[] query = queries[i];
            int[] path1 = getPath(query[0], query[1]);
            int[] path2 = getPath(query[2], query[3]);
            result[i] = count(path1, path2);
        }

        return result;
    }

    private static void buildTree(int[][] tree) {
        // полное дерево
        for (int i = 0, size = tree.length; i < size; i++) {
            Node n1 = Node.of(tree[i][0]);
            Node n2 = Node.of(tree[i][1]);
            Node parent = n1.peerQu >= n2.peerQu || n1.n == 1 ? n1 : n2;
            Node slave = n1.peerQu >= n2.peerQu ? n2 : n1;
            parent.add(slave);
            slave.setParent(parent);
        }

        int[] buf = new int[N];
        tailIdentifier = new int[N + 1];
        tailCore = new int[N + 1];

        // хвосты
        for (Node node : Node.map.values()) {
            if (node.peerQu == 1) {
                int tailID = node.n;
                // строим хвост до узла с > 2 пирами
                // длина хвоста
                int h = 0;
                // на вылете там будет ядро или пусто или пасажир особо длинного хваоста
                Node n;
                for (n = node; n.parent != null && n.peerQu < 3 && h < N; n = n.parent, ++h) {
                    int t = n.n;
                    buf[h] = t;
                    tailIdentifier[t] = tailID;
                }

                // хвост
                int[] tail = new int[h];
                System.arraycopy(buf, 0, tail, 0, h);

                // *из хвоста -> узел ядра
                if (n.peerQu > 2) {
                    // узел ядра
                    int core = n.n;
                    for (int i = 0; i < tail.length; ++i)
                        tailCore[tail[i]] = core;
                }

                corePerimeter.add(n.n);

                // tailID -> tail
                tailMap.put(tailID, tail);
            }
        }

    }

    // private static void buildCore() {
    //     if (corePerimeter.size() == 1)
    //         return;
    //
    //     // строим снизу вверх
    //     HashSet<Integer> level;
    //     for (level = corePerimeter; level.size() > 1; /* BEWARE */) {
    //
    //         HashSet<Integer> next = new HashSet<>();
    //
    //         for (Integer n : level) {
    //             Node node = Node.of(n);
    //             HashSet<Integer> freePeers = node.freePeers;
    //             if (freePeers.size() == 1) {
    //                 for (Integer pn : freePeers) {
    //                     Node parent = Node.of(pn);
    //                     // dual side link
    //                     node.setParent(parent);
    //                     parent.freePeers.remove(node.n);
    //                     // build new level
    //                     next.add(pn);
    //                     break;
    //                 }
    //             }
    //         }
    //
    //         level = next;
    //     }
    // }

    private static int[] getPath(int src, int dst) {
        // понять расположение src, dst ибо возожны варианты:
        // оба в одном хвосте
        // оба в разных хвостах, оба в ядре тогда идентификаторы хвостов === 0, один в ядре другой нет
        int srcTailID = tailIdentifier[src];
        int dstTailID = tailIdentifier[dst];
        if (srcTailID == dstTailID && srcTailID != 0 /*&& dstTailID != 0*/) {
            return getSingleTailPath(src, dst, tailMap.get(srcTailID));
        }
        else {
            // возможен пустой массив
            int[] srcTailPath = srcTailID > 0 ? getTailToCorePath(src, tailMap.get(srcTailID)) : new int[]{};
            int[] dstTailPath = dstTailID > 0 ? getTailToCorePath(dst, tailMap.get(dstTailID)) : new int[]{};
            // точка вход в ядро
            int srcCore = srcTailID > 0 ? tailCore[srcTailID] : src;
            int dstCore = dstTailID > 0 ? tailCore[dstTailID] : dst;
            int[] corePath = getCorePath(srcCore, dstCore);

            int[] result = new int[srcTailPath.length + corePath.length + dstTailPath.length];
            System.arraycopy(srcTailPath, 0, result, 0, srcTailPath.length);
            System.arraycopy(corePath, 0, result, srcTailPath.length, corePath.length);
            System.arraycopy(dstTailPath, 0, result, srcTailPath.length + corePath.length, dstTailPath.length);
            return result;
        }
    }

    private static int[] getCorePath(int src, int dst) {
        if (src == dst) {
            return new int[]{src};
        }
        else if (corePerimeter.size() == 1) {
            // крата вылетов из ядра
            // public static HashMap<Integer, HashSet<Integer>> exitMap = new HashMap<>();
            // Ядро из одного узлв
            int singleCoreNode = 0;
            return new int[]{singleCoreNode};
        }
        else if (corePerimeter.size() == 2) {
            return new int[]{src, dst};
        }
        else {
            LinkedList<Node> srcList = new LinkedList<>();
            LinkedList<Node> dstList = new LinkedList<>();

            for (Node n = Node.of(src); n != null; n = n.parent)
                srcList.add(n);

            for (Node n = Node.of(dst); n != null; n = n.parent)
                dstList.add(n);

            Node common = null;
            for (; !srcList.isEmpty() && !dstList.isEmpty() && srcList.getLast() == dstList.getLast(); ) {
                common = srcList.getLast();
                srcList.removeLast();
                dstList.removeLast();
            }

            srcList.add(common);
            Collections.reverse(dstList);
            srcList.addAll(dstList);

            int[] result = srcList.stream().mapToInt(node -> node.n).toArray();

            return result;
        }
    }

    // c ообоих хвостового конца до головы хвоста включительно
    private static int[] getTailToCorePath(int src, int[] tail) {
        int i;
        for (i = 0; i < tail.length; ++i) if (tail[i] == src) break;
        int[] range = Arrays.copyOfRange(tail, i, tail.length);
        return range;
    }

    // c ообоих концов одновременно i <= j by design
    private static int[] getSingleTailPath(int src, int dst, int[] tail) {
        int i, j;
        for (i = 0; i < tail.length; ++i) if (tail[i] == src || tail[i] == dst) break;
        for (j = tail.length - 1; j >= 0; --j) if (tail[j] == src || tail[j] == dst) break;

        int[] range = Arrays.copyOfRange(tail, i, j + 1);
        return range;
    }

    public static void main(String[] args) throws IOException {
        final Scanner scanner = new Scanner(System.in);
        final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        //final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("c:/temp/OUTPUT_PATH.txt"));

        int n = scanner.nextInt();
        int q = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; ++i) {
            values[i] = scanner.nextInt();
        }
        int[][] tree = new int[n - 1][2];
        for (int i = 0; i < n - 1; ++i) {
            tree[i][0] = scanner.nextInt();
            tree[i][1] = scanner.nextInt();
        }
        int[][] queries = new int[q][4];
        for (int i = 0; i < q; ++i) {
            queries[i][0] = scanner.nextInt();
            queries[i][1] = scanner.nextInt();
            queries[i][2] = scanner.nextInt();
            queries[i][3] = scanner.nextInt();
        }

        int[] result = solve(values, tree, queries);

        for (int i : result) {
            bufferedWriter.write(String.valueOf(i));
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        scanner.close();
    }

    static class Node {
        final int n;
        Node parent;
        int peerQu = 0;

        private Node(int n) {
            this.n = n;
        }

        // registry
        static Node of(int n) {
            Node node = map.get(n);
            if (node == null) {
                node = new Node(n);
                map.put(n, node);
            }
            return node;
        }

        // можно заменить массивом Node[N+1]
        private static HashMap<Integer, Node> map = new HashMap<>();

        void setParent(Node parent) {
            this.parent = parent;
            ++peerQu;
        }

        void add(Node slave) {
            ++peerQu;
        }

        @Override
        public String toString() {
            return String.format("%d -> %s", n, parent != null ? parent.n : "null");
        }

    }
}

