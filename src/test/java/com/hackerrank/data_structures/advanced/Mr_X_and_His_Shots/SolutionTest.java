package com.hackerrank.data_structures.advanced.Mr_X_and_His_Shots;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

import static com.hackerrank.data_structures.advanced.Mr_X_and_His_Shots.Solution.*;

/**
 * @author program@globall.ru, 21.10.2019
 */
public class SolutionTest {
    // TEST CASE #
    private static final int TEST_CASE = 0;//17;

    @Test
            //(timeout = 4_000)
    public void test() throws FileNotFoundException {
        long t0 = System.nanoTime();
        Scanner scanner = new Scanner(new File(String.format("D:\\hackerrank\\src\\test\\java\\com\\hackerrank\\data_structures\\advanced\\Mr_X_and_His_Shots\\input%02d.txt", TEST_CASE))).useLocale(Locale.US);
        Object[] o = load(scanner);
        int[][] shots = (int[][]) o[0];
        int[][] players = (int[][]) o[1];
        int result = solve(shots, players);
        long t1 = System.nanoTime();
        System.err.printf("%.3f\n", (t1 - t0) * 0.000_000_001);

        Assert.assertEquals(result,9);
        //Assert.assertEquals(result,147_644_462);
    }
}