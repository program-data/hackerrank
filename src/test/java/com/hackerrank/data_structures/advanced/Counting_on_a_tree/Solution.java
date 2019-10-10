package com.hackerrank.data_structures.advanced.Counting_on_a_tree;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
public class Solution {

    // Complete the solve function below.
    static int[] solve(int[] c, int[][] tree, int[][] queries) {

        return new int[]{};
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

        int[][] tree = new int[n-1][2];

        for (int treeRowItr = 0; treeRowItr < n-1; treeRowItr++) {
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

        int[][] tree = new int[n-1][2];

        for (int treeRowItr = 0; treeRowItr < n-1; treeRowItr++) {
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

        return new Object[]{c,tree, queries};
    }

    @Test
    public void test() throws FileNotFoundException {
        Object[] objects = load("D:/hackerrank/src/test/java/com/hackerrank/data_structures/advanced/Counting_on_a_tree/input00.txt");
        Assert.assertArrayEquals(new int[]{0,1,3,2,0}, solve((int[]) objects[0], (int[][])objects[1], (int[][])objects[2]));

    }
}

