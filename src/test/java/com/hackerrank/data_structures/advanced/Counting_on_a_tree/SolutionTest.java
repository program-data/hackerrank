package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * @author michael.malevannyy@gmail.com, 19.10.2019
 */

public class SolutionTest {
    // TEST CASE #
    private static final int TEST_CASE = 0;

    @Test//(timeout = 15_000)
    public void test() throws FileNotFoundException {
        long t0 = System.nanoTime();
        Object[] objects = load(String.format("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/input%02d.txt", TEST_CASE));
        int[] values = (int[]) objects[0];
        int[][] tree = (int[][]) objects[1];
        int[][] queries = (int[][]) objects[2];
        solution = Solution.solve(values, tree, queries);
        long t1 = System.nanoTime();
        System.err.printf("%.3f\n", (t1 - t0) * 0.000_000_001);
    }

    private int[] solution;

    @After
    public void after() throws FileNotFoundException {
        int[] answer = loadAnswer(String.format("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/output%02d.txt", TEST_CASE));
        Assert.assertArrayEquals(answer, solution);
    }

    private Object[] load(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path)).useLocale(Locale.US);
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

        return new Object[]{values, tree, queries};
    }

    private int[] loadAnswer(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path)).useLocale(Locale.US);
        List<Integer> list = new ArrayList<>();
        while (scanner.hasNextInt())
            list.add(scanner.nextInt());

        return list.stream().mapToInt(value -> value).toArray();
    }
}