package com.hit.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class calshortestpaths {
    private static Map<String, Boolean> inQueue = new HashMap<>();
    private static Map<String, Integer> dis = new HashMap<>();
    private static final int INF = 0x3f3f3f3f;
    private static List<String> tmpPath = new ArrayList<>();
    private static List<String> paths = new ArrayList<>();


    private static void init(Map<String, Map<String, Integer>> G) {
        //初始化数据结构和距离数组
        inQueue.clear();
        dis.clear();
        for (String node : G.keySet()) {
            inQueue.put(node, false);
            dis.put(node, INF);
        }
    }


    //用于计算源节点到途中其他节点的最短路径 时间复杂度O(VE)
    private static void spfa(Map<String, Map<String, Integer>> G, String st) {
        List<String> Q = new ArrayList<>();
        String u;
        init(G);
        Q.add(st);
        inQueue.put(st, true);
        dis.put(st, 0);
        while (!Q.isEmpty()) {
            u = Q.get(0);
            Q.remove(0);
            inQueue.put(u, false);
            for (Map.Entry<String, Integer> entry : G.get(u).entrySet()) {
                String v = entry.getKey();
                int weight = entry.getValue();
                if (dis.get(v) > dis.get(u) + weight) {
                    dis.put(v, dis.get(u) + weight);
                    if (!inQueue.get(v)) {
                        Q.add(v);
                        inQueue.put(v, true);
                    }
                }
            }
        }
    }

    //深度有限搜索
    private static void dfs(Map<String, Map<String, Integer>> G, String u, String ed) {
        if (u.equals(ed)) {
            StringBuilder path = new StringBuilder();
            for (String v : tmpPath) {
                path.append(v).append("->");
            }
            path.append(ed);
            paths.add(path.toString());
            return;
        }

        for (Map.Entry<String, Integer> entry : G.get(u).entrySet()) {
            String v = entry.getKey();
            int weight = entry.getValue();
            if (dis.get(v) == dis.get(u) + weight) {
                tmpPath.add(u);
                dfs(G, v, ed);
                tmpPath.remove(tmpPath.size() - 1);
            }
        }
    }


    public static List<String> calcShortestPath(Map<String, Map<String, Integer>> G, String word1, String word2) {
        tmpPath.clear();
        paths.clear();
        spfa(G, word1);
        dfs(G, word1, word2);
        return paths;
    }

}