package edu.mergesort;

import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(sort(new int[]{1, 2, 3, 4})));
        System.out.println(Arrays.toString(sort(new int[]{4, 3, 2, 1})));
        System.out.println(Arrays.toString(sort(new int[]{2, 1, 3, 4})));
        System.out.println(Arrays.toString(sort(new int[]{1, 1, 4, 4})));

        var time = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            sort(new int[]{4, 3, 2, 1});
        }
        System.out.println(System.currentTimeMillis()-time);
    }

    private static int[] sort(int[] ints) {

        return divide(ints, 0, ints.length-1);

    }

    private static int[] divide(int[] m1, int from, int to) {
        var diff = to - from;
        int med = diff / 2;
        if (diff == 0) {
            return new int[]{m1[from]};
        } else if (diff == 1) {
            if (m1[from] > m1[to]) {
                return new int[]{m1[to], m1[from]};
            } else {
                return new int[]{m1[from], m1[to]};
            }
        } else {
            return merge(divide(m1, from, med), divide(m1, med + 1, to));
        }
    }

    private static int[] merge(int[] m1, int[] m2) {

        var r = new int[m1.length * m2.length];
        int m1m = 0;
        int m2m = 0;

        for (int i = 0; i < r.length; i++) {
            if (m1.length == m1m) {
                r[i] = m2[m2m++];
            } else if (m2.length == m2m) {
                r[i] = m1[m1m++];
            } else if (m1[m1m] < m2[m2m]) {
                r[i] = m1[m1m++];
            } else {
                r[i] = m2[m2m++];
            }
        }

        return r;

    }


}
