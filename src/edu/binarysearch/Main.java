package edu.binarysearch;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        System.out.println(bs(new int[]{1,2,3,4,5}, 4)); //3
        System.out.println(bs(new int[]{1,2,3,4,5}, 1)); //0
        System.out.println(bs(new int[]{1,2,3,4,5}, 5)); //4
        System.out.println(bs(new int[]{1,2,3,4,5}, 7)); //-1
        System.out.println(bs(new int[]{1,2,3,4,5}, 0)); //-1
    }

    public static int bs(int[] array, int el){
        return bs(array, el, 0, array.length-1);
    }

    public static int bs(int[] array, int el, int l, int r){

        if (l==r && array[l]==el)
            return l;
        else if (l==r)
            return -1;

        var ls=l+(r-l)/2;
        var rs=ls+1;

        if (array[l]<=el && array[ls]>=el){
            return bs(array, el, l, ls);
        }else {
            return bs(array, el, rs, r);
        }

    }

}
