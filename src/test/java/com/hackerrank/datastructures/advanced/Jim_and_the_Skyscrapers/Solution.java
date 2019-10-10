package com.hackerrank.datastructures.advanced.Jim_and_the_Skyscrapers;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * @author michael.malevannyy@gmail.com, 10.10.2019
 */
public class Solution {

    //static int H = 1_000_000+1;
    // Complete the solve function below.
    static long solve(int[] arr) {

        TreeMap<Integer, Integer> tree = new TreeMap<>();
        long k = 0;

        for (int i = 0; i < arr.length; ++i) {
            int a = arr[i];

            // 1) закрыть свои
            Integer h = tree.get(a);
            if(h != null) {
                ++h;
                tree.put(a, h);
                k+=h;
            }
            else {
                // 3) добавить себя, новый, если еще нет нас
                tree.put(a,0);
            }

            // 2) прибить меньших
            //tree.removeIf(integer -> integer < a);
            tree.keySet().removeIf(integer -> integer < a);
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
