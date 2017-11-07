package fourth;

import javafx.util.Pair;
import model.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final List<Integer> start = Arrays.asList(4, 8, 1, null, 3, 6, 2, 7, 5);
    public static final List<Integer> end = Arrays.asList(6, null, 8, 5, 2, 1, 4, 3, 7);

    public static void main(String[] args) {
        Vertex frontRoot = new Vertex(null, start);
        Vertex tailRoot = new Vertex(null, end);
        Pair<Vertex, Vertex> result = null;
        List<Vertex> frontList = new ArrayList<>();
        List<Vertex> tailList = new ArrayList<>();
        Set<List<Integer>> frontSet = new HashSet<>();
        Set<List<Integer>> tailSet = new HashSet<>();
        List<Vertex> frontChildren = new ArrayList<>();
        List<Vertex> tailChildren = new ArrayList<>();
        frontList.add(frontRoot);
        tailList.add(tailRoot);
        frontSet.add(start);
        tailSet.add(end);
        int count = 0;
        long startTime = System.currentTimeMillis();
        while (true) {
            for (Vertex vertex : frontList) {
                frontChildren.addAll(expandVertex(vertex, frontSet));
            }
            count += frontChildren.size();
            result = getResult(frontChildren, tailList);
            if (result == null) {
                frontList.clear();
                frontList.addAll(frontChildren);
                frontChildren = new ArrayList<>();
            } else {
                break;
            }

            for (Vertex vertex : tailList) {
                tailChildren.addAll(expandVertex(vertex, tailSet));
            }
            count += tailChildren.size();
            result = getResult(frontList, tailChildren);
            if (result == null) {
                tailList.clear();
                tailList.addAll(tailChildren);
                tailChildren = new ArrayList<>();
            } else {
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        List<Vertex> first = getPath(result.getKey(), true);
        List<Vertex> second = getPath(result.getValue(), false);
        List<Vertex> fullPath = new ArrayList<>();
        fullPath.addAll(first);
        fullPath.addAll(second);
        for(Vertex vertex:fullPath){
            System.out.println(toString(vertex));
        }
        System.out.println("Total depth: " + fullPath.size());
        System.out.println("Opened nodes: " + count);
        System.out.println("Computed in " + (endTime - startTime) + " milliseconds");
    }


    public static List<Vertex> expandVertex(Vertex parent, Set<List<Integer>> set) {
        int zeroIndex = getZeroPosition(parent);
        List<Vertex> list = new ArrayList<>();
        Vertex vertex;
        if (zeroIndex - 3 >= 0) {
            vertex = new Vertex();
            vertex.parent = parent;
            vertex.value = swap(parent.value, zeroIndex, zeroIndex - 3);
            list.add(vertex);
        }
        if (zeroIndex + 3 <= 8) {
            vertex = new Vertex();
            vertex.parent = parent;
            vertex.value = swap(parent.value, zeroIndex, zeroIndex + 3);
            list.add(vertex);
        }
        if (zeroIndex % 3 > 0) {
            vertex = new Vertex();
            vertex.parent = parent;
            vertex.value = swap(parent.value, zeroIndex, zeroIndex - 1);
            list.add(vertex);
        }
        if (zeroIndex % 3 < 2) {
            vertex = new Vertex();
            vertex.parent = parent;
            vertex.value = swap(parent.value, zeroIndex, zeroIndex + 1);
            list.add(vertex);
        }
        return list.stream().filter(v -> set.add(v.value)).collect(Collectors.toList());
    }

    public static List<Integer> swap(List<Integer> src, int i, int j) {
        List<Integer> dst = new ArrayList<>();
        for (int k = 0; k < src.size(); k++) {
            Integer value = src.get(k);
            dst.add(value);
        }
        Integer e = dst.get(i);
        dst.set(i, dst.get(j));
        dst.set(j, e);
        return dst;
    }

    public static int getZeroPosition(Vertex parent) {
        int position = 0;
        for (Integer i : parent.value) {
            if (i == null) {
                break;
            }
            position++;
        }
        return position;
    }

    public static void printResult(Vertex vertex) {
        while (vertex.parent != null) {
            System.out.println(toString(vertex));
            vertex = vertex.parent;
        }
        System.out.println(toString(vertex));
    }

    static List<Vertex> getPath(Vertex vertex, boolean reverse) {
        List<Vertex> path = new ArrayList<>();
        Vertex v;
        if (!reverse)
            v = vertex.parent;
        else
            v = vertex;
        while (v.parent != null) {
            path.add(v);
            v = v.parent;
        }
        path.add(v);
        if (reverse) {
            Collections.reverse(path);
        }
        return path;
    }

    public static String toString(Vertex vertex) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            Integer value = vertex.value.get(i);
            if (value == null) value = 0;
            builder.append(value + "\t");
            if ((i + 1) % 3 == 0 && i != 0 && i != 8) builder.append("\n");
        }
        builder.append("\n\n");
        return builder.toString();
    }

    public static Pair<Vertex, Vertex> getResult(List<Vertex> front, List<Vertex> tail) {
        for (Vertex frontVertex : front) {
            for (Vertex tailVertex : tail) {
                if (equals(frontVertex.value, tailVertex.value)) {
                    return new Pair<>(frontVertex, tailVertex);
                }
            }
        }
        return null;
    }

    public static boolean equals(List<Integer> first, List<Integer> second) {
        for (int i = 0; i < 9; i++) {
            if (first.get(i) != second.get(i)) return false;
        }
        return true;
    }
}
