package edu.joinvector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main3 {

    public static void main(String[] args) {

        var usedB = usedM();

        var inc = new AtomicInteger();
        var sets = Stream.generate(() -> 1)
            .limit(100_000)
            .map(v -> Stream.generate(inc::incrementAndGet).limit(1_000)
                .collect(Collectors.toList()))
            .peek(v -> v.add(inc.get() / 10_000))
            .collect(Collectors.toList());

        System.out.println("begin " + (usedM() - usedB) / 1024 / 1024);
        usedB = usedM();
        var time = System.currentTimeMillis();
        joinInputs(sets);
        System.out.println(System.currentTimeMillis() - time);

        System.out.println("end " + (usedM() - usedB) / 1024 / 1024);
    }

    /**
     * @param sets - groups of inputs which may have crossed addresses
     * @return key - all addresses in inputs; value - groups of inputs which NOT have crossed
     * addresses
     */
    public static Set<Set<Integer>> joinInputs(List<List<Integer>> sets) {

        var graphIndex = initIndex(sets);
        joinNodes(graphIndex);
        return collectUnCrossedClusters(graphIndex);

    }

    private static long usedM() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private static Set<Set<Integer>> collectUnCrossedClusters(Map<Integer, GraphNode> graphIndex) {
        var handled = new HashSet<Integer>();

        var ret = new HashSet<Set<Integer>>();

        for (var link : graphIndex.entrySet()
        ) {

            var cluster = walkThrough(handled, link.getValue(), null);
            if (cluster != null) {
                var clusterData = new HashSet<Integer>();
                for (var unJoined : cluster) {
                    clusterData.addAll(unJoined.data);
                }
                ret.add(clusterData);
            }
        }
        return ret;
    }

    private static void joinNodes(Map<Integer, GraphNode> graphIndex) {
        Map<Integer, GraphNode> addressIndex = new HashMap<>();

        for (var newNode : graphIndex.values()) {
            for (var address : newNode.data) {

                addressIndex.computeIfPresent(address, (key, oldValue) -> {
                    oldValue.link(newNode);
                    return newNode;
                });

                addressIndex.putIfAbsent(address, newNode);
            }
        }

    }

    private static Map<Integer, GraphNode> initIndex(List<List<Integer>> sets) {
        var graphIndex = new HashMap<Integer, GraphNode>();

        for (var set : sets) {
            graphIndex.put(set.hashCode(), new GraphNode(set));
        }

        return graphIndex;
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

        private final Set<GraphNode> graphNodes = new HashSet<>();
        public final List<Integer> data;

        public GraphNode(List<Integer> data) {
            this.data = data;
        }

        public void link(GraphNode node) {
            graphNodes.add(node);
            node.graphNodes.add(this);
        }
    }

}
