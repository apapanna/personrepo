package com.bmuschko.testcontainers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindCycle {

    public int[] root;
    public int[] rank;

    public FindCycle(int size) {

        root = new int[size];
        rank = new int[size];

        for(int i = 0 ; i < size; i++) {
            root[i] = i;
            rank[i] = 1;
        }
    }

    public int find(int x) {
        if(x == root[x]) {
            return x;
        }
        return root[x] = find(root[x]);
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if(rootX != rootY) {
            if(rank[rootX] > rank[rootY]) {
                root[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                root[rootX] = rootY;
            } else {
                root[rootY] = rootX;
                rank[rootX] += 1;
            }
            return false;
        } else if (x != y && rootX == rootY) {
            return true;
        }
        return false;
    }

    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }

    public static void main(String[] args) {

        int[][] edges = {{0,1},{1,2},{2,3},{1,4}};
        int[][] edges1 = {{0,1},{0,2},{1,2},{1,3},{1,4}};
        int[][] edges3 = {{0,1}, {2,3}};
        int size = 5;
        FindCycle uf = new FindCycle(size);

        for(int i = 0; i < edges.length ; i++) {
            //check for cycle everytime you try to perform union.
            if(uf.union(edges[i][0], edges[i][1])) {
                System.out.println("There's a cycle");
            }
        }

        //check if it's a valid tree by verifying there's more than 1 unique root in the root array.
        //uf.find(uf.root[0]) instead of just uf.root[0] to make sure stale roots are updated and used at the time of comparison
        int uniqueroot = uf.find(uf.root[0]);
        for(int k = 0; k < uf.root.length; k++) {
            if(uf.find(uf.root[k]) != uniqueroot) {
                System.out.println("Not a valid tree");
                break;
            }
        }
    }

    }

