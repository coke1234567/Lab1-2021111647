package com.hit.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides methods to calculate the shortest paths
 * in a graph using the SPFA algorithm and to find all paths
 * with the shortest distance using DFS.
 */
public class CalcShortestPaths {
  private static final Map<String, Boolean> inQueue = new HashMap<>();
  private static final Map<String, Integer> dis = new HashMap<>();
  private static final int INF = 0x3f3f3f3f;
  private static final List<String> tmpPath = new ArrayList<>();
  private static final List<String> paths = new ArrayList<>();


  private static void init(Map<String, Map<String, Integer>> graph) {
    //初始化数据结构和距离数组
    inQueue.clear();
    dis.clear();
    for (String node : graph.keySet()) {
      inQueue.put(node, false);
      dis.put(node, INF);
    }
  }


  //用于计算源节点到途中其他节点的最短路径 时间复杂度O(VE)
  private static void spfa(Map<String, Map<String, Integer>> graph, String st) {
    List<String> queue = new ArrayList<>();
    init(graph);
    queue.add(st);
    inQueue.put(st, true);
    dis.put(st, 0);
    while (!queue.isEmpty()) {
      final String currentNode = queue.get(0);
      queue.remove(0);
      inQueue.put(currentNode, false);
      for (Map.Entry<String, Integer> entry : graph.get(currentNode).entrySet()) {
        String v = entry.getKey();
        int weight = entry.getValue();
        if (dis.get(v) > dis.get(currentNode) + weight) {
          dis.put(v, dis.get(currentNode) + weight);
          if (!inQueue.get(v)) {
            queue.add(v);
            inQueue.put(v, true);
          }
        }
      }
    }
  }

  //深度有限搜索
  /**
 * Performs a depth-first search to find all paths with the shortest distance
 * from the start node to the end node.
 */
  private static void dfs(Map<String, Map<String, Integer>> graph, String u, String ed) {
    if (u.equals(ed)) {
      StringBuilder path = new StringBuilder();
      for (String v : tmpPath) {
        path.append(v).append("->");
      }
      path.append(ed);
      paths.add(path.toString());
      return;
    }

    for (Map.Entry<String, Integer> entry : graph.get(u).entrySet()) {
      String v = entry.getKey();
      int weight = entry.getValue();
      if (dis.get(v) == dis.get(u) + weight) {
        tmpPath.add(u);
        dfs(graph, v, ed);
        tmpPath.remove(tmpPath.size() - 1);
      }
    }
  }

  /**
     * Calculates the shortest path from the start node to the end node in the graph.
     *
     * @return A list of strings representing the shortest paths.
   */
  public static List<String> calcShortestPath(Map<String, Map<String, Integer>> graph,
                                              String word1, String word2) {
    tmpPath.clear();
    paths.clear();
    spfa(graph, word1);
    dfs(graph, word1, word2);
    return paths;
  }
}