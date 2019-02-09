package com.hackerrank;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author michael.malevannyy@gmail.com, 02.02.2019
 */

public class TravelAroundTheWorld {

    private static int travelAroundTheWorld(final int[] a, final int[] b, final long c) {
        final int N = a.length;

        // предрассчитанные дельты
        int[] d = new int[N];
        // возможные = 0, невозможные -1
        int[] imp = new int[N];

        // размечаем дельты и сразу считаем баланс
        boolean chocolate = true;
        long sigma = 0;
        for (int i = 0; i < N; i++) {
            int di = d[i] = ((int) (a[i] < c ? a[i] : c) - b[i]);  // delta(a[i], b[i], c);
            sigma += di;
            if (chocolate && di < 0)
                chocolate = false;
        }

        // шоколад
        if (chocolate)
            return N;

        // абзац
        if (sigma < 0)
            return 0;

        // долг <=0 by design
        int borrow = 0;

        // бежим по дельтам в обратную сторону в поисках дырок,
        // p дополнительно ускоренно сдвигается на самую младшую пройденную дырку
        // если есть непогашенный долг тогда можно пойти на второй круг, но не третий
        for (int p = N - 1; (borrow < 0 || p >= 0) && p > -N; --p) {
            // цикический индекс
            int i = p >= 0 ? p : p + N;

            // текущая дельта
            int di = d[i];

            if (borrow >= 0 && di >= 0)
                continue;

            // это долг не сможет перейти через эту точку ибо её проход занимает весь безнобак
            if(borrow < 0 && b[i] == c)
                return 0;

            // если долга не было он может как появиться так и погасться так и не измениться, но точно borrow <=0
            borrow += di;

            // by design borrow <=0
            if (borrow > 0)
                borrow = 0;

            // вычёркиваем
            if (borrow < 0)
                imp[i] = 1;
        }

        int qu = 0;
        for (int i = 0; i < N; i++) {
            if (imp[i] == 0)
                qu++;
        }

        return qu;
    }

//    private static int delta(int a, int b, long c) {
//        return ((int) (a < c ? a : c) - b);
//    }

    @Test
    public void test() {
        assertEquals(2, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 3L));
        assertEquals(1, travelAroundTheWorld(new int[]{2, 1, 3}, new int[]{2, 2, 2}, 3L));
        assertEquals(1, travelAroundTheWorld(new int[]{2, 2, 1, 3}, new int[]{2, 2, 2, 2}, 3L));
        assertEquals(1, travelAroundTheWorld(new int[]{1, 4, 1}, new int[]{2, 2, 2}, 4L));
        assertEquals(1, travelAroundTheWorld(new int[]{4, 1, 1}, new int[]{2, 2, 2}, 4L));
        assertEquals(0, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 2L));


        assertEquals(0, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 2L));

        // fu..up :( мелкий бензобак
        assertEquals(0, travelAroundTheWorld(new int[]{2, 2, 1}, new int[]{1, 2, 2}, 2L));
    }
}

