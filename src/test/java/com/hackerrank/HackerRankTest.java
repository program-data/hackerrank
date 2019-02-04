package com.hackerrank;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author michael.malevannyy@gmail.com, 02.02.2019
 */

public class HackerRankTest {

    static int travelAroundTheWorldStupid(int[] a, int[] b, final long c) {
        int counter = 0;
        int n = a.length;

        // меняем стартовый город
        for (int k = 0; k < n; k++) {
            if (isSuccess(a, b, c, n, k)) {
                counter++;

                System.out.println(k);
            }
        }

        return counter;
    }

    private static boolean isSuccess(int[] a, int[] b, long c, int n, int k) {
        // fuel;
        long f = 0;
        // проход по маршруту
        for (int j = 0; j < n; j++) {
            // индекс в цеелвом массиве
            int i = j + k < n ? j + k : j + k - n;
            // остаток к концу шага
            f = c - a[i] > f ? f + a[i] : c;
            assertTrue(f <= c);
            f -= b[i];

            if (f < 0)
                return false;
        }
        return true;
    }

    static int travelAroundTheWorld(final int[] a, final int[] b, final long c) {
        int n = a.length;
        // <= -1
        int unaccepable = 0;
        // заем
        long borrow = 0;

        // уменьшаемый счетчмк шагов за которые надо погасить долг, если q <= 0 то круг вообще непроходим => вернуть 0;
        int q = n;

        // идем против шерсти
        for (int i = n - 1; i >= 0; i--) {
            long delta = (a[i] > c ? c : a[i]) - b[i];

            if (delta < 0) {
                // если нет непогашенного долга то сбрасываем счётчк шагов для поиска гашения
                if (borrow == 0)
                    q = n;

                unaccepable++;
            }

            if (delta == 0 && borrow > 0)
                unaccepable++;

            // списание накопление
            borrow -= delta;

            // но нельзя кредитовать банк
            if(borrow <= 0)
                borrow = 0;
        }

        return n - unaccepable;
    }

    @Test
    public void test() {
        assertEquals(2, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 3L));

        assertEquals(1, travelAroundTheWorld(new int[]{2, 1, 3}, new int[]{2, 2, 2}, 3L));

    }

}