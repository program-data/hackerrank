package com.hackerrank;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * https://www.hackerrank.com/challenges/xor-quadruples/problem
 *
 * @author michael.malevannyy@gmail.com, 18.02.2019
 */

public class BeautifulQuadruples {

    static int beautifulQuadruples(int a, int b, int c, int d) {
        int q = 0;

        // сорт вем
        List<Integer> list = Arrays.asList(a, b, c, d);
        Collections.sort(list);
        a = list.get(0);
        b = list.get(1);
        c = list.get(2);
        d = list.get(3);

        int ah = fillRight(Integer.highestOneBit(a));
        int bh = fillRight(Integer.highestOneBit(b));
        int ch = fillRight(Integer.highestOneBit(c));

        for (int i = 1; i <= a; ++i) {
            for (int j = i; j <= b; ++j) {
                for (int k = j; k <= c; ++k) {
                    for (int t = k; t <= d; ++t) {

                        int x = i ^ j ^ k ^ t;
                        if (x > 0)
                            ++q;

                        //System.out.printf("%d %d %d %d = %d %s\n", i, j, k, t, x, x > 0 ? String.format("(%d)", q) : "");

                        if (ch == t) {
                            q += (d - t);
                            break;
                        }
                    }
                }
            }
        }


        return q;
    }

    private static int fillRight(int oneBit) {
        int accum = oneBit;
        for (int m = 1; m < oneBit; m <<= 1)
            accum &= m;

        return accum;
    }

    @Test
    public void test() {
        Assert.assertEquals(11, beautifulQuadruples(1, 2, 3, 4));
        Assert.assertEquals(31, beautifulQuadruples(1, 2, 3, 8));
    }

    @Test
    public void test1() {
        for (int i = 0; i < 256; i++) {
            System.out.printf("%d -> %d, %d\n", i, Integer.highestOneBit(i), 32 - Integer.numberOfLeadingZeros(i));
        }
    }
}
