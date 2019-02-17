package com.hackerrank;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author michael.malevannyy@gmail.com, 17.02.2019
 */

public class GradingStudents {
    static int[] gradingStudents(int[] grades) {
        final int k = 5;
        final int tolerance = 3;
        for (int i = 0; i < grades.length; i++) {
            int grade = grades[i];
            if (grade >= 38) {
                if (grade % k > 0) {
                    int next5 = (grade / k + 1) * k;
                    if (next5 - grade < tolerance) {
                        grades[i] = next5;
                    }
                }

            }
        }
        return grades;
    }


    @Test
    public void test() {
        Assert.assertArrayEquals(new int[]{75, 67, 40, 33}, gradingStudents(new int[]{73, 67, 38, 33}));
    }

}
