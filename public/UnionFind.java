package com.bmuschko.testcontainers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UnionFind {

    public int[] root;
    public int[] rank;

    public UnionFind(int size) {

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

    public void union(int x, int y) {
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
        }
    }

    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }

    public static void main(String[] args) {
        int size = 6;
        int province = 0;
        UnionFind unionFind = new UnionFind(size);

        int[][] isConnected = {{1,1,0,0,0,0},{1,1,0,0,0,0},{0,0,1,1,1,0},{0,0,1,1,0,0},{0,0,1,0,1,0}, {0,0,0,0,0,1}};

        for(int i= 0; i < isConnected.length; i++) {
            for (int j = 0; j < isConnected[i].length; j++) {
                if (isConnected[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        Set<Integer> uniqueSet  = new HashSet<>();
        Map<Integer,Set<Integer>> provinces = new HashMap<>();
        Set<Integer> getProvinceSet = new HashSet<>();

        for(int i=0 ; i< unionFind.root.length; i++) {
            if(provinces.get(unionFind.root[i]) != null) {
                getProvinceSet = provinces.get(unionFind.root[i]);
            } else {
                getProvinceSet = new HashSet<>();
            }
            getProvinceSet.add(i);
            provinces.put(unionFind.root[i], getProvinceSet);

            if(!uniqueSet.contains(unionFind.root[i])) {
                uniqueSet.add(unionFind.root[i]);
                province++;
            }
        }

        System.out.println("Here : " + province);
        }
    }

