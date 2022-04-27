package edu.tranc;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        var m1= new int[][]{
                new int[]{1,2,3,4},
                new int[]{5,6,7,8},
                new int[]{9,10,11,12},
                new int[]{13,14,15,16}
        };
        var iter=1;
//        for (int i = 1; i < m1.length; i++) {
//            for (int j = 0; j < iter; j++) {
//                var tmp=m1[i][j];
//                m1[i][j]=m1[j][i];
//                m1[j][i]=tmp;
//            }
//            iter++;
//        }

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1.length; j++) {
                System.out.print(m1[j][i]+";");
            }
            System.out.println();

        }

    }
}
