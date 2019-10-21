package com.hackerrank.data_structures.advanced.Mr_X_and_His_Shots;

import java.io.*;
import java.util.*;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public class Solution {

    // Complete the solve function below.
    static int solve(int[][] shots, int[][] players) {
        int sum = 0;

        // markup
        int N = 100_000_000;
        int[] m = new int[N];

        short got = 0;
        byte delta = 0;

        int j = 0;
        int[] shot = shots[j];

        for (int i = 0; i < N; ++i) {
            int s = shot[0];
            int e = shot[1];

            if (i < s) {
                // nop
            }
            else if (i >= s && i<e) {
                ++got;
            }


            m[i] = delta << 20 | got;
        }

        return sum;
    }


    public static void main(String[] args) throws IOException {
        final Scanner scanner = new Scanner(System.in);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        Object[] o = load(scanner);
        int[][] shots = (int[][]) o[0];
        int[][] players = (int[][]) o[1];
        int result = solve(shots, players);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedWriter.close();
        scanner.close();
    }

    static Object[] load(Scanner scanner) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int[][] shots = new int[n][2];
        for (int i = 0; i < n; ++i) {
            shots[i][0] = scanner.nextInt();
            shots[i][1] = scanner.nextInt();
        }
        int[][] players = new int[m][2];
        for (int i = 0; i < m; ++i) {
            players[i][0] = scanner.nextInt();
            players[i][1] = scanner.nextInt();
        }
        return new Object[]{shots, players};
    }
}
