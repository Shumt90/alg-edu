package edu.joinvector;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main2 {
    public static void main(String[] args) {

        var usedB = usedM();

        var inc = new AtomicInteger();
//        var sets = Stream.generate(() -> 1)
//                .limit(1000)
//                .map(v -> Stream.generate(inc::incrementAndGet).limit(10000).collect(Collectors.toList()))
//                .peek(v -> v.add(inc.get() / 1000))
//                .collect(Collectors.toList());
        var sets = Stream.generate(()->1)
                .limit(100_000)
                .map(v->Stream.generate(inc::incrementAndGet).limit(1_000).collect(Collectors.toList()))
                .peek(v->v.add(inc.get()/10_000))
                .collect(Collectors.toList());

        System.out.println("begin " + (usedM() - usedB) / 1024 / 1024);
        usedB = usedM();
        var time = System.currentTimeMillis();
        joinInputs(sets);
        System.out.println(System.currentTimeMillis() - time);

        System.out.println("end " + (usedM() - usedB) / 1024 / 1024);
    }

//    public static void main(String[] args) {
//
//
//        System.out.println(joinInputs(List.of(
//                of(5, 7, 9, 10),
//                of(3, 4, 7, 12)
//        )));
//
//        System.out.println(joinInputs(List.of(
//                of(5, 7, 9, 10),
//                of(3, 4, 8, 12),
//                of(3, 5, 13, 14, 18, 120),
//                of(1001, 1002, 1003, 1004, 1005)
//        )));
//
//        System.out.println(joinInputs(List.of(
//                of(5, 5, 5, 5),
//                of(5, 5, 5, 5)
//        )));
//    }

    private static List<Integer> of(int... ints) {
        var l = new ArrayList<Integer>(ints.length);
        for (int i : ints) {
            l.add(i);
        }
        return l;
    }

    private static long usedM() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * @param sets - groups of inputs which may have crossed addresses
     * @return key - all addresses in inputs; value -  groups of inputs which NOT have crossed
     * addresses
     */
    private static List<Set<Integer>> joinInputs(List<List<Integer>> sets) {
        var unlinkedNodeSize = sets.size();
        var graphIndex = new HashMap<Integer, GraphNode>();
        for (List<Integer> set : sets) {
            set.sort(Comparator.naturalOrder());
            var newGraph = new GraphNode(set);
            graphIndex.put(set.hashCode(), newGraph);

        }

        for (int i = 0; i < unlinkedNodeSize; i++) {
            var set1 = sets.get(i);
            for (int j = i + 1; j < unlinkedNodeSize; j++) {
                var set2 = sets.get(j);

                int i1 = 0;
                int i2 = 0;
                Integer el1;
                Integer el2;

                while (set1.size() > i1 && set2.size() > i2) {

                    el1 = set1.get(i1);
                    el2 = set2.get(i2);

                    if (el1.equals(el2)) {
                        graphIndex.get(set1.hashCode()).link(graphIndex.get(set2.hashCode()));
                        break;
                    }

                    if (el1 < el2) {
                        i1++;
                    } else {
                        i2++;
                    }

                }

            }
        }

        var handled = new HashSet<Integer>();

        var ret = new ArrayList<Set<Integer>>();


        for (var link : graphIndex.entrySet()
        ) {

            var cluster = walkThrough(handled, link.getValue(), null);
            if (cluster != null) {
                ret.add(cluster.stream().flatMap(g -> g.data.stream()).collect(Collectors.toSet()));
            }

        }

        return ret;
    }

    private static List<GraphNode> walkThrough
            (Set<Integer> handled, GraphNode node, List<GraphNode> cluster) {

        var currentNodeIndexKey = node.data.hashCode();
        if (handled.contains(currentNodeIndexKey)) {
            return cluster;
        }

        handled.add(currentNodeIndexKey);

        if (cluster == null) {
            cluster = new LinkedList<>();
        }

        cluster.add(node);

        for (var innerNode : node.graphNodes) {
            var inner = walkThrough(handled, innerNode, cluster);

            if (inner != null) {
                cluster.addAll(inner);
            }
        }

        return cluster;

    }

    private static class GraphNode {
        private final List<GraphNode> graphNodes = new LinkedList<>();
        public List<Integer> data;

        public void link(GraphNode node) {
            graphNodes.add(node);
            node.graphNodes.add(this);
        }

        public GraphNode(List<Integer> set) {
            this.data = set;
        }
    }

}
