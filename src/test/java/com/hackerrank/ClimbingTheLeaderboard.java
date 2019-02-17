package com.hackerrank;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author michael.malevannyy@gmail.com, 17.02.2019
 */

public class ClimbingTheLeaderboard {
    // Complete the climbingLeaderboard function below.
    static int[] climbingLeaderboard(int[] scores, int[] alice) {
        // calc init ranks
        TreeMap<Integer, Integer> ranks = new TreeMap<>(Comparator.reverseOrder());

        for (int score : scores) {
            ranks.put(score, 0);
        }

        int r = 0;
        for (Map.Entry<Integer, Integer> entry : ranks.entrySet()) {
            entry.setValue(++r);
        }

        ++r;

        int[] climbing = new int[alice.length];

        for (int i = 0, length = alice.length; i < length; i++) {

            Map.Entry<Integer, Integer> ceilingEntry = ranks.ceilingEntry(alice[i]);
            climbing[i] = ceilingEntry != null ? ceilingEntry.getValue() : r;
        }

        return climbing;
    }

    @Test
    public void test() {
        Assert.assertArrayEquals(new int[]{6, 5, 4, 2, 1}, climbingLeaderboard(new int[]{100, 90, 90, 80, 75, 60}, new int[]{50, 65, 77, 90, 102}));
    }
}
