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

    // 3 пиешм хвосты в карту на основе индексов массива: элементв хвоста -> узел ядра
    private static void writeTailToMap(int n) {
        // потенцияльно это можно заменить огромным переиспользуемым буфером потом скопировать только заполненную часть, может оказаться быстрее
        // TODO refactor .contains() to n-2
        List<Integer> list = new ArrayList<>(12);
        for (int[] peer = Solution.matrix[n]; peer.length < 3; n = !list.contains(peer[0]) ? peer[0] : peer[1], peer = Solution.matrix[n]) {
            list.add(n);
        }
        // sux
        int[] tail = list.stream().mapToInt(i -> i).toArray();

        // 3 пиешм хвосты в карту хвост -> ядро
        for (int t : tail) {
            // индетивицируем хвост его краем.
            tailIdentifier[t] = tail[0];
            // и это тоже
            tailCore[t] = n;
            // и так
            core.add(n);
        }

        // пишем весь хвост в его порядке в крту хвостов
        tailMap.put(tail[0], tail);
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
    // комутатор, *хвост -> иднетификатор хвоста  (по его кончику)
    private static int[] tailIdentifier;
    // коммутатор *хвост -> ядро сети
    private static int[] tailCore;
    // карта хвостов
    // https://stackoverflow.com/questions/7057430/treemap-or-hashmap-faster
    // https://avaldes.com/wp-content/uploads/2014/11/MapPerformance.png
    private static HashMap<Integer, int[]> tailMap = new HashMap<>();
    // колекция узлов ядра
    private static HashSet<Integer> core = new HashSet<>();
    // Ядро из одного узлв
    private static int singleCoreNode = 0;

    // queries === array[k][4]
    static int[] solve(int[] values, int[][] tree, int[][] queries) {
        Solution.N = values.length;
        Solution.values = values;
        tailIdentifier = new int[N + 1];
        tailCore = new int[N + 1];
        // 1
        Solution.matrix = buildCompressedMatrix(tree);

        // 2 фильтруем и отбираем только с 1 соседом = концы хвостов
        for (int i = 1, size = matrix.length; i < size; i++) {
            int[] peer = Solution.matrix[i];
            if (peer.length == 1) {
                writeTailToMap(i);
            }
        }

        // частный кейс
        if(core.size() == 1) {
            singleCoreNode = core.stream().findFirst().get();
        }


        // 3 из ядерных узлов, это те что не хвосты один раз строим дерево по которому будет ходить потом искалка маршрутов


        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int[] query = queries[i];
            int[] path1 = getPath(query[0], query[1]);
            int[] path2 = getPath(query[2], query[3]);
            result[i] = count(path1, path2);
        }

        return result;
    }

    private static int[] getPath(int src, int dst) {
        // понять расположение src, dst ибо возожны варианты:
        // оба в одном хвосте
        // оба в разных хвостах, оба в ядре тогда мдентификаторы хвостов === 0, один в ядре другой нет
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
            int dstCore = dstTailID > 0 ? tailCore[dstTailID] : src;
            int[] corePath = getCorePath(srcCore, dstCore);

            int[] result = new int[srcTailPath.length + corePath.length + dstTailPath.length];
            System.arraycopy(srcTailPath, 0, result, 0, srcTailPath.length);
            System.arraycopy(corePath, 0,    result, srcTailPath.length, corePath.length);
            System.arraycopy(dstTailPath, 0, result, srcTailPath.length + corePath.length, dstTailPath.length);
            return result;
        }
    }

    private static int[] getCorePath(int src, int dst) {
        if(core.size() == 1) {
            return new int[]{singleCoreNode};
        }

        return new int[]{};
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

