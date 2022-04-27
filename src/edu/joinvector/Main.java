package edu.joinvector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        var usedB=usedM();

        var inc=new AtomicInteger();
        var sets = Stream.generate(()->1)
                .limit(100_000)
                .map(v->Stream.generate(inc::incrementAndGet).limit(1_000).collect(Collectors.toSet()))
                .peek(v->v.add(inc.get()/10_000))
                .collect(Collectors.toList());


        System.out.println("begin "+(usedM()-usedB)/1024/1024);
        usedB=usedM();
        var time = System.currentTimeMillis();
        joinInputs(sets);
        System.out.println(System.currentTimeMillis()-time);

        System.out.println("end "+(usedM()-usedB)/1024/1024);
    }

    private static long usedM(){
        return Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    }

    /**
     * @param sets - groups of inputs which may have crossed addresses
     * @return key - all addresses in inputs; value -  groups of inputs which NOT have crossed
     * addresses
     */
    private static HashMap<Integer, Set<Integer>> joinInputs(List<Set<Integer>> sets) {
        var map = new HashMap<Integer, Set<Integer>>();

        for (var set : sets) {
            for (var el : set) {
                map.computeIfPresent(el, (key, oldValue) -> {
                    oldValue.addAll(set);
                    return oldValue;
                });
                map.putIfAbsent(el, set);
            }
        }

        return map;
    }
}
