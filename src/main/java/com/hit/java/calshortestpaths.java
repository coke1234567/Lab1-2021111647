package com.hit.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

public class calshortestpaths {
    private static Map<String, Boolean> inQueue = new HashMap<>();
    private static Map<String, Integer> dis = new HashMap<>();
    private static final int INF = 0x3f3f3f3f;
    private static List<String> tmpPath = new ArrayList<>();
    private static List<String> paths = new ArrayList<>();

    /**
     * Init auxiliary array for spfa.
     * <p>
     * inQueue: whether the node is in the queue.<br>
     * dis: the distance from source to the node.
     *
     * @param G
     */
    private static void init(Map<String, Map<String, Integer>> G) {
        inQueue.clear();
        dis.clear();
        for (String node : G.keySet()) {
            inQueue.put(node, false);
            dis.put(node, INF);
        }
    }

    /**
     * calculate the distance from source to all other node by spfa.
     *
     * @param G Graph
     * @param st source node
     */
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

    /**
     * find all path by dfs on the shortest path graph.
     *
     * @param G Graph
     * @param u now node
     * @param ed target node
     */
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

    /**
     * calculate all shortest path from node word1 to word2.
     *
     * @param G Graph
     * @param word1 String
     * @param word2 String
     * @return a list of all shortest path. each path is like this<br>
     *         A-&gt;B-&gt;C
     */
    public static List<String> calcShortestPath(Map<String, Map<String, Integer>> G, String word1, String word2) {
        tmpPath.clear();
        paths.clear();
        if ((word1 != null && !G.containsKey(word1)) || (word2 != null && !G.containsKey(word2)))
            return paths;
        spfa(G, word1);
        dfs(G, word1, word2);
        return paths;
    }

    /**
     * calculate all distance from node word1 to word2 without path.
     *
     * @param G Graph
     * @param word1 String
     * @param word2 String
     * @return Integer distance
     */
    public static Integer calcPathDistance(Map<String, Map<String, Integer>> G, String word1, String word2) {
        spfa(G, word1);
        if (dis.get(word2).equals(INF))
            return -1;
        else
            return dis.get(word2);
    }
}