package second;

import model.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final List<Integer> start = Arrays.asList(4, 8, 1, null, 3, 6, 2, 7, 5);
    public static final List<Integer> end = Arrays.asList(6, null, 8, 5, 2, 1, 4, 3, 7);

    public static void main(String[] args) {
        Vertex root = new Vertex(null, start);
        Vertex result = null;
        List<Vertex> vertexList = new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();
        vertexList.add(root);
        set.add(start);
        boolean flag = false;
        List<Vertex> children;
        int nextVertexIndex;
        int count = 0;
        int childrenSize = 0;
        long startTime = System.currentTimeMillis();
        while (!flag) {
            nextVertexIndex = nextVertex(vertexList, childrenSize);
            Vertex vertex = vertexList.get(nextVertexIndex);
            children = expandVertex(vertex, set);
            childrenSize = children.size();
            count += children.size();
            for (Vertex v : children) {
                v.computeH2(end);
            }
            result = getResult(children);
            if (result == null) {
                insert(nextVertexIndex, children, vertexList);
            } else {
                flag = true;
            }
        }
        long endTime = System.currentTimeMillis();
        printResult(result);
        System.out.println("Opened nodes: "+count);
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
        int i = 0;
        while (vertex.parent != null) {
            System.out.println(toString(vertex));
            vertex = vertex.parent;
            i++;
        }
        System.out.println(toString(vertex));
        System.out.println("Total depth: "+i);
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

    public static void insert(int nextVertexIndex, List<Vertex> children, List<Vertex> leaves) {
        leaves.remove(nextVertexIndex);
        leaves.addAll(0, children);
    }

    public static int nextVertex(List<Vertex> leaves, int childrenSize) {
        int min = 100;
        int i = -1;
        boolean flag = false;
        for (int j = 0; j < childrenSize; j++) {
            model.Vertex vertex = leaves.get(j);
            if (vertex.h2 < vertex.parent.h2) {
                i++;
                flag = true;
            }
        }
        if (flag) {
            return i;
        }
        i = -1;
        for (model.Vertex vertex : leaves) {
            if (vertex.h2 <= min) {
                min = vertex.h2;
                i++;
            }
        }
        return i;
    }

    public static Vertex getResult(List<Vertex> leaves) {
        for (Vertex vertex : leaves) {
            if (vertex.h2 == 0) return vertex;
        }
        return null;
    }
}
