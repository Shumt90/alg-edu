package edu.selectedsort;

import java.util.Arrays;

public class Main {
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

        time = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            insertionSort(new int[]{4, 3, 2, 1});
        }
        System.out.println(System.currentTimeMillis()-time);
    }

    private static int[] sort(int... items) {
        int n = items.length;
        var minI = 0;
        var minV = 0;
        for (int i = 0; i < n; i++) {
             minI = i;
             minV = items[i];
            for (int j = i+1; j < n; j++) {
                if (items[j] < minV) {
                    minI = j;
                    minV = items[j];
                }
            }
            var tmp = items[i];
            items[i] = minV;
            items[minI] = tmp;
        }
        return items;
    }

    private static void insertionSort (int arr[])
    {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }


}
