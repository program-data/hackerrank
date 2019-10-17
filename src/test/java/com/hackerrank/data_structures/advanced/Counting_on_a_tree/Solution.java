package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
@SuppressWarnings("WeakerAccess")
public class Solution {
    // Complete the solve function below.

    // tree === array[n-1][2]
    private static int[][] buildCompressedMatrix(int[][] tree) {
        // long t0 = System.nanoTime();
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

        // special unused case, init it for safety only
        matrix[0] = new int[]{};

        // long t1 = System.nanoTime();  System.err.printf("%.3f\n",(t1-t0)*0.000_000_001); // ~0.1c on test #4
        return matrix;
    }

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

    // compressed direction matrix
    private static int N;
    private static int[] values;
    private static int[][] matrix;
    // комутатор, карта *хвост -> ядро сети
    private static int[] tailToCoreMap;
    // выбор выходя из ядра
    private static int[] exitRouter;
    // мегакоммутатор хвостовы вылета, пока карта
    static Map<Integer, Map<Integer, int[]>> map = new TreeMap<>();

    // queries === array[k][4]
    static int[] solve(int[] values, int[][] tree, int[][] queries) {
        Solution.N = values.length;
        Solution.values = values;
        tailToCoreMap = new int[N + 1];
        exitRouter = new int[N+1];
        // 1
        Solution.matrix = buildCompressedMatrix(tree);

        // 2 фильтруем и отбираем только с 1 соседом = концы хвостов
        for (int i = 1, size = matrix.length; i < size; i++) {
            int[] peer = Solution.matrix[i];
            if (peer.length == 1) {
                writeTail(i);
            }
        }

        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int[] query = queries[i];
            int[] path1 = getPath(query[0], query[1]);
            int[] path2 = getPath(query[2], query[3]);
            result[i] = count(path1, path2);
        }

        return result;
    }

    // 3 пиешм хвосты в карту хвост -> ядро
    // 5 собранные хвосты разворачиваем головй вперед и складываем в коммутоатор вылетов
    private static void writeTail(int n) {
        List<Integer> list = new ArrayList<>(12);
        // TODO refactor .contains()
        for (int[] peer = Solution.matrix[n]; peer.length < 3; n = !list.contains(peer[0]) ? peer[0] : peer[1], peer = Solution.matrix[n]) {
            list.add(n);
        }
        // головой вперед
        Collections.reverse(list);
        // sux
        int[] tail = list.stream().mapToInt(i -> i).toArray();

        // 3 пиешм хвосты в карту хвост -> ядро
        for (int t : tail) {
            tailToCoreMap[t] = tail[0];
            exitRouter[t] = tail[0];
        }

        // 5 складываем в мега коммутоатор вылетов
        Map<Integer, int[]> exit = map.get(n);
        if (exit != null) {
            exit.put(tail[0], tail);
        }
        else {
            exit = new TreeMap<>();
            exit.put(tail[0], tail);
            map.put(n, exit);
        }
    }

    private static int[] getPath(int src, int dst) {
        List<Integer> path = new ArrayList<>();

        // понять расположение src, dst ибо возожно что:
        // оба в ядре тогда точки входа будут == 0
        // оба в одном хвосте тогда у них будет одинаковая точка входа

        // хвосты ростить надо тоже одновременно, это позволит избавиться от мегакоммутатора вылета и выходных хвостов!
        // более того порядок в пути не имеет никакого смясла, можно просто валить кучей ибо потом будет сортится по значениям



        // хвост входа, однако мы можем попасть и не в хвост а сразу в ядро
        // на вызходе из цикла там будет точка вход в ядро
        int srcCore = src;
        for (int[] peer = Solution.matrix[srcCore]; peer.length < 3; srcCore = !path.contains(peer[0]) ? peer[0] : peer[1], peer = Solution.matrix[srcCore]) {
            path.add(srcCore);

            // можем всё решить хвостом
            if(srcCore == dst)
                break;
        }

        if(srcCore != dst) {
            // целевая точка выхода из ядра , межет быть 0 т.е. выходная точка внутри ядра
            int dstCore = tailToCoreMap[dst];

            // TODO путь по ядру

            // выходной хвост
            if (dstCore != 0) {

                Map<Integer, int[]> exitMap = map.get(dstCore);

                int[] tail = exitMap.get(exitRouter[dst]);
                // хвост вылета
                for (int i = 0, size = tail.length; i < size; i++) path.add(tail[i]);
            }

        }

        return path.stream().mapToInt(i -> i).toArray();
    }

    /// UNTIL HERE

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("c:/temp/OUTPUT_PATH.txt"));

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

    private int[] c;
    private int[][] tree;
    private int[][] queries;
    private int[] solution;
    private int[] answer;

    @Before // ~13-16 cек
    public void before() throws FileNotFoundException {
        int n = 0;
        Object[] objects = load(String.format("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/input%02d.txt", n));
        c = (int[]) objects[0];
        tree = (int[][]) objects[1];
        queries = (int[][]) objects[2];
        answer = loadAnswer(String.format("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/output%02d.txt", n));
    }

    @Test//(timeout = 15_000)
    public void test() {
        solution = solve(c, tree, queries);
    }

    @After
    public void after() {
        Assert.assertArrayEquals(answer, solution);
    }

    private int[] loadAnswer(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path)).useLocale(Locale.US);
        List<Integer> list = new ArrayList<>();
        while (scanner.hasNextInt())
            list.add(scanner.nextInt());

        return list.stream().mapToInt(value -> value).toArray();
    }
}

