package com.hackerrank.datastructures.advanced.Jim_and_the_Skyscrapers;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
public class Solution {

    // Complete the solve function below.
    static int H = 1_000_000+1;

    static long solve(int[] arr) {

        int[] h = new int[H];

        long k = 0;

        for (int i = 0; i < arr.length; ++i) {
            int a = arr[i];

            // 1) закрыть свои
            k+=h[a];
            ++h[a];

            // 2) прибить меньших
            if(i>0 && arr[i-1] < a) {
                for (int j = 0; j < a; ++j)
                    h[j] = 0;
            }

        }

        return 2*k;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int arrCount = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[arrCount];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int arrItr = 0; arrItr < arrCount; arrItr++) {
            int arrItem = Integer.parseInt(arrItems[arrItr]);
            arr[arrItr] = arrItem;
        }

        long result = solve(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }

    @Test
    public void test() throws FileNotFoundException {
        Assert.assertEquals(8, solve(new int[]{3,2,1,2,3,3}));
        Assert.assertEquals(0, solve(new int[]{1,2,1}));
        Assert.assertEquals(14, solve(new int[]{3,2,1,2,3,3,3}));

        int[] arr = load("d:/download/input13.txt");
        Assert.assertEquals(22731434672L, solve(arr));

    }

    public static int[] load(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        int arrCount = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[arrCount];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int arrItr = 0; arrItr < arrCount; arrItr++) {
            int arrItem = Integer.parseInt(arrItems[arrItr]);
            arr[arrItr] = arrItem;
        }

        return arr;
    }

}
