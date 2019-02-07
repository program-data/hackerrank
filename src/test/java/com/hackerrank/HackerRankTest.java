package com.hackerrank;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author michael.malevannyy@gmail.com, 02.02.2019
 */

public class HackerRankTest {

    private static int travelAroundTheWorld(final int[] a, final int[] b, final long c) {
        final int N = a.length;
        int qu = N;

        int[] d = new int[N];

        // pre-scan
        List<Integer> negativeIndexes = new ArrayList<>(); // linkedlist
        for (int i = 0; i < N; i++) {
            int di = d[i] = delta(a[i], b[i], c);
            if (di < 0)
                negativeIndexes.add(i);
        }

        // шоколад
        if (negativeIndexes.isEmpty())
            return N;

        qu -= negativeIndexes.size();

        // negativeIndexes по определению упорядочены по росту индексов, берем самый старший
        int j = negativeIndexes.get(negativeIndexes.size() - 1);

        // начальная дельта для самого крайнего негативного узлф, шатано отрицательная
        int delta = delta(a[j], b[j], c);

        assertTrue(delta < 0);

        // откусываем отхвоста негативных
        negativeIndexes.remove(negativeIndexes.size() - 1);

        // начинаем с предыдущего
        --j;

        // идем по кольцу обратно но не более N-1 шагов, за которые надо порешать все отрицательный дельты,
        // если выходим из цикла поесе полного прохода то return 0 ибо не разрешили все вараинты
        for (int k = 0; k < N - 1; ++k, --j) {
            // приземлённый индекс
            int i = j >= 0 ? j : j + N;

            int di = d[i];

            if(negativeIndexes.contains(i))
            {
                negativeIndexes.remove(negativeIndexes.size() - 1);
            }

            // если нет непогашенного долга то пропускаем и это не негативнй
            if (delta >= 0 && di >= 0) {
                delta = 0;
                continue;
            }

            // положительну дельту возможно оставшуюся с предыдущего шага нельзя накапливать, можно только гасить
            if (delta > 0)
                delta = 0;

            // гасим или еще больше уменьшаем дельту
            delta += di;

            // елси этот пасажир не гасит накопленный следущими долг то из из этой точки выдвикаться нелья
            if (delta < 0)
                --qu;

            if(delta >=0 && negativeIndexes.isEmpty())
                return qu;
        }

        return 0;
    }

    private static int delta(int a, int b, long c) {
        return  ((int)(a < c ? a : c) - b);
    }

    @Test
    public void test() {
        assertEquals(2, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 3L));
        assertEquals(1, travelAroundTheWorld(new int[]{2, 1, 3}, new int[]{2, 2, 2}, 3L));
        assertEquals(1, travelAroundTheWorld(new int[]{2, 2, 1, 3}, new int[]{2, 2, 2, 2}, 3L));
        assertEquals(1, travelAroundTheWorld(new int[]{1, 4, 1}, new int[]{2, 2, 2}, 4L));
        assertEquals(1, travelAroundTheWorld(new int[]{4, 1, 1}, new int[]{2, 2, 2}, 4L));
        assertEquals(0, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 2L));


        assertEquals(0, travelAroundTheWorld(new int[]{3, 1, 2}, new int[]{2, 2, 2}, 2L));
    }
}


//    private static int travelAroundTheWorld(final int[] a, final int[] b, final long c) {
//        int counter = 0;
//        int N = a.length;
//
//        // pre-scan
//        List<Integer> negativeIndexes = new ArrayList<>();
//        for (int i = 0; i < N; i++) {
//            if(a[i]<b[i])
//                negativeIndexes.add(i);
//        }
//
//        // шоколад
//        if(negativeIndexes.isEmpty())
//            return N;
//
//        // придётся жевать варианты
//
//        // меняем стартовый город
//        for (int k = 0; k < N; k++) {
//            // нефиг его торкать
//            if(negativeIndexes.contains(k))
//                continue;
//
//            if (isSuccess(a, b, c, N, k, negativeIndexes)) {
//                counter++;
//
//                System.out.println(k);
//            }
//        }
//
//        return counter;
//    }
//
//    private static boolean isSuccess(int[] a, int[] b, long c, int n, int k, List<Integer> negativeIndexes) {
//        int negativeVisitedQu = 0;
//        int negativeIndexesSize = negativeIndexes.size();
//
//        // fuel;
//        long f = 0;
//        // проход по маршруту
//        for (int j = 0; j < n; j++) {
//            // индекс в цеелвом массиве
//            int i = j + k < n ? j + k : j + k - n;
//            // остаток к концу шага
//            f = c - a[i] > f ? f + a[i] : c;
//            assertTrue(f <= c);
//            f -= b[i];
//
//            if (f < 0)
//                return false;
//
//            if(negativeIndexes.contains(i))
//                negativeVisitedQu++;
//
//            if(negativeVisitedQu >= negativeIndexesSize)
//                return true;
//        }
//        return true;
//    }
