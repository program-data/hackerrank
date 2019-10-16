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

        return matrix;
    }

    // [path]
    private static int[] getPath(int src, int dst) {
        int[] path = new int[N];
        path[0] = src;

        int k = recursive(path, 0, src, dst);

        // can bee faster ?
        int[] result = new int[k];
        System.arraycopy(path, 0, result, 0, k);
        return result;
    }

    // маркер глубины кроличьей норы // bad practice global variable :(
    private static int deepEnd;

    private static int recursive(int[] path, int n, int current, int dst) {
        deepEnd = 0;

        path[n++] = current;
        if (current == dst)
            return n;

        // todo чем дальше dst от медвежьего угла тем быстрее будет брать из кэша
        if(cache[current].length > 0 && cache[current][dst] > 0) {
            int next = cache[current][dst];
            int k = recursive(path, n, next, dst);
            if (k > 0) {
                return k;
            }

            return 0;
        }
        else
        {
            // тупой поиск если еще нет в кэше
            int p = n > 1 ? path[n - 2] : 0;

            int[] matrix = Solution.matrix[current];

            for (int c : matrix) {
                if (c != p) {
                    int k = recursive(path, n, c, dst);
                    if (k > 0) {
                        return k;
                    }
                    else {
                        // шанс прописать в кэш
                        if (cache[current].length > 0) {
                            for (int i = deepEnd; i >= n; --i)
                                cache[current][path[i]] = c;
                        }
                    }
                }
            }

            // насколько глубоуо мы залезли
            if (n - 1 > deepEnd)
                deepEnd = n - 1;

            return 0;
        }
    }

    private static int solve(int[] query) {
        int k = 0;
        int[] path1 = getPath(query[0], query[1]);
        int[] path2 = getPath(query[2], query[3]);

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
    static int[][] matrix;
    static int[] values;
    private static int N;
    private static boolean[][] boo;

    // кэш
    private static int[][] cache;

    // queries === array[k][4]
    static int[] solve(int[] values, int[][] tree, int[][] queries) {
        Solution.matrix = buildCompressedMatrix(tree);
        matrix[0] = new int[]{};
        long count = Arrays.stream(matrix).filter(ints -> ints.length > 2).count();
        boo = new boolean[(int) count][N];

        Solution.values = values;
        Solution.N = values.length;

        // инициализируем
        cache = new int[N + 1][];
        // нулями
        for (int i = 0, size = matrix.length; i < size; i++)
            cache[i] = Solution.matrix[i].length > 1 ? new int[N + 1] : new int[0];


        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            result[i] = solve(queries[i]);
        }

        return result;
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
        Object[] objects = load("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/input04.txt");
        c = (int[]) objects[0];
        tree = (int[][]) objects[1];
        queries = (int[][]) objects[2];
        answer = loadAnswer("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/output04.txt");
    }

    @Test(timeout = 00_000)
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

