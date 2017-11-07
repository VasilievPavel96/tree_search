package model;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    public Vertex parent;
    public List<Integer> value;
    public int h1;
    public int h2;
    public List<Vertex> children = new ArrayList<>();

    public Vertex() {
    }

    public Vertex(Vertex parent, List<Integer> value) {
        this.parent = parent;
        this.value = value;
    }

    public void computeH1(List<Integer> end) {
        for (int i = 0; i < 9; i++) {
            Integer n1 = value.get(i);
            Integer n2 = end.get(i);
            if (n1 != null) {
                if (n1.equals(n2)) {
                    h1++;
                }
            } else {
                if (n2 == null) {
                    h1++;
                }
            }
        }
    }

    public void computeH2(List<Integer> end) {
        int i, j;
        for (i = 0; i < 9; i++) {
            Integer n1 = value.get(i);
            Integer n2;
            if (n1 == null) {
                n1 = 0;
            }
            for (j = 0; j < 9; j++) {
                n2 = end.get(j);
                if (n2 == null) {
                    n2 = 0;
                }
                if (n1 == n2) {
                    break;
                }
            }
            int y1 = i / 3;
            int x1 = i % 3;
            int y2 = j / 3;
            int x2 = j % 3;
            h2 += Math.abs(y2 - y1) + Math.abs(x2 - x1);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
