package edu.annogram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Some {
    public static void main(String[] args) throws IOException {

        var lines = Files.readAllLines(Path.of("RUS.txt"));

        var time1 = System.currentTimeMillis();
        alg1(lines);
        System.out.println("alg1: " + (System.currentTimeMillis() - time1));

        time1 = System.currentTimeMillis();
        alg2(lines);
        System.out.println("alg2: " + (System.currentTimeMillis() - time1));

    }

    private static void alg2(List<String> lines) {

        var na=lines.stream()
                .map(word->Map.entry(sign(word), word))
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());



        var count = new AtomicInteger();


        String prev = "@";
        int cc = 0;

        for (var word : na) {
            if (prev.equals(word.getKey())){
                cc++;
            } else {
                if (cc>=1){
                    count.incrementAndGet();
                }
                cc=0;
            }
            prev = word.getKey();
        }

        System.out.println("count: " + count.incrementAndGet());
    }

    private static String sign(String word) {
        var ca = word.toCharArray();
        Arrays.sort(ca);
        return new String(ca);
    }

    private static void alg1(List<String> lines) {
        var count = new AtomicInteger();
        lines.stream()
                .filter(word -> word.length() > 1)
                .collect(Collectors.groupingBy(Some::sign))
                .forEach((key, value) -> {
                    if (value.size() > 1) {
                        count.incrementAndGet();
                    }
                });

        System.out.println("count: " + count.incrementAndGet());

    }
}
