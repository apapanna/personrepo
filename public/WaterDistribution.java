package com.example.demo;

import java.util.*;

class UnionFind {
    /**
     * Implementation of UnionFind without load-balancing.
     */
    private int[] root;
    private int[] rank;

    public UnionFind(int size) {
        // container to hold the group id for each member
        // Note: the index of member starts from 1,
        //   thus we add one more element to the container.
        root = new int[size + 1];
        rank = new int[size + 1];
        for (int i = 0; i < size + 1; ++i) {
            root[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * return the group id that the person belongs to.
     */
    public int find(int x) {
        if (root[x] != x) {
            root[x] = find(root[x]);
        }
        return root[x];
    }

    /**
     * Join the groups together.
     * return:
     * false when the two persons belong to the same group already,
     * otherwise true
     */
    public boolean union(int person1, int person2) {
        int rootX = find(person1);
        int rootY = find(person2);
        if (rootX == rootY) {
            return false;
        }

        // attach the group of lower rank to the one with higher rank
        if (rank[rootX] > rank[rootY]) {
            root[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            root[rootX] = rootY;
        } else {
            root[rootX] = rootY;
            rank[rootY] += 1;
        }

        return true;
    }
}


class WaterDistribution {
    public static void main(String[] args) {
        int n = 3;
        int[] wells = {5,5,5};
        int[][] pipes = {{1,2,1}, {2,3,1}};

        WaterDistribution wd = new WaterDistribution();
        System.out.println(wd.minCostToSupplyWater(3, wells, pipes));
    }

    public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
        List<int[]> orderedEdges = new ArrayList<>(n + 1 + pipes.length);

        // add the virtual vertex (index with 0) along with the new edges.
        for (int i = 0; i < wells.length; ++i) {
            orderedEdges.add(new int[]{0, i + 1, wells[i]});
        }

        // add the existing edges
        for (int i = 0; i < pipes.length; ++i) {
            int[] edge = pipes[i];
            orderedEdges.add(edge);
        }

        // sort the edges based on their cost
        //Collections.sort(orderedEdges, (a, b) -> a[2] - b[2]);

        //compare(o1, o2) { first = o1[2] ; second = o2[2] ; first - second }

        // iterate through the ordered edges
        UnionFind uf = new UnionFind(n);
        int totalCost = 0;
        Set<Integer> nodes = new HashSet<>();
        List<int[]> paths = new ArrayList<>();
        for (int[] edge : orderedEdges) {
            int house1 = edge[0];
            int house2 = edge[1];
            int cost = edge[2];
            // determine if we should add the new edge to the final MST
            if (uf.union(house1, house2)) {
                paths.add(new int[]{house1, house2});
                totalCost += cost;
            }
        }

        return totalCost;
    }
}
