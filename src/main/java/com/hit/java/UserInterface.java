package com.hit.java;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * A UI class for visualizing and interacting with a directed graph
 * generated from a text file.
 */
public class UserInterface extends JFrame {
  private final Map<String, Map<String, Integer>> graph;
  private final JTextArea textArea;
  private final JFileChooser fileChooser;
  private final JTextField word1Field;
  private final JTextField word2Field;
  private final JTextField newTextField;
  private final AtomicBoolean stopRequested = new AtomicBoolean(false);
  // 在UserInterface类中声明SecureRandom实例
  private final SecureRandom secureRandom = new SecureRandom();

  private  boolean flag = true;

  /**
   * The main method to start the UI.
   *
   * @param args Command line arguments.
  */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
    public void run() {
        new UserInterface();
      }
    });
  }

  /**
    * Constructs the UI and initializes the components.
  */
  public UserInterface() {
    graph = new HashMap<>();
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setFont(new Font("TimesNewRoman", Font.PLAIN, 20));
    fileChooser = new JFileChooser();
    //从文件中读入文本生成有向图
    JButton loadButton = new JButton("Load Text File");
    loadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fileChooser.showOpenDialog(UserInterface.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
              File file = fileChooser.getSelectedFile();
              processTextFile(file.getAbsolutePath());
              GraphVisualizer.showDirectedGraph(graph);
            }
        }
    });

    //两个文本的输入框
    word1Field = new JTextField(10);
    word2Field = new JTextField(10);
    // 设置 JTextField 的字体大小
    word1Field.setFont(new Font("TimesNewRoman", Font.PLAIN, 20)); // 设置字体为 Arial，样式为常规，大小为 20
    word2Field.setFont(new Font("TimesNewRoman", Font.PLAIN, 20)); // 设置字体为 Arial，样式为常规，大小为 20
    //查询桥接词
    JButton queryButton = new JButton("Query Bridge Words");
    queryButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String word1 = word1Field.getText();
          String word2 = word2Field.getText();
          if (graph.isEmpty()) {
            textArea.setText("");
            textArea.append("Please press Load Text File button first!\n");
          } else {
            String result = queryBridgeWords(word1, word2);
            System.out.println(result);
            textArea.setText("");
            textArea.append(result + "\n");
          }
        }
    });

    //生成新文本
    newTextField = new JTextField(20);
    newTextField.setFont(new Font("TimesNewRoman", Font.PLAIN, 20)); // 设置字体为 Arial，样式为常规，大小为 20
    JButton generateButton = new JButton("Generate New Text");
    generateButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String inputText = newTextField.getText();
          if (graph.isEmpty()) {
            textArea.setText("");
            textArea.append("Please press Load Text File button first!\n");
          } else {
            textArea.setText("");
            String result = generateNewText(inputText);
            textArea.append(result + "\n");
        }
        }
    });

    //计算最短路径
    JButton shortestPathButton = new JButton("Calculate Shortest Path");
    shortestPathButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        String word1 = word1Field.getText();
        String word2 = word2Field.getText();
        if (graph.isEmpty()) {
          textArea.setText("");
          textArea.append("Please press Load Text File button first!\n");
        } else {
            textArea.setText("");
            if (word1 != null && word2 != null) { // 确保 word1 和 word2 不为 null
                if (word1.isEmpty() && !word2.isEmpty()) {
                  flag = true;
                  // 遍历图中的所有节点，计算源节点到所有节点的最短路径
                  for (String node : graph.keySet()) {
                    if (!node.equals(word2)) {
                      String shortestPath = calcShortestPath(node, word2);
                      textArea.append(shortestPath + "\n");
                    }
                  }
                } else if (word2.isEmpty() && !word1.isEmpty()) {
                  flag = false;
                  // 遍历图中的所有节点，计算源节点到所有节点的最短路径
                  for (String node : graph.keySet()) {
                    if (!node.equals(word1)) {
                      String shortestPath = calcShortestPath(word1, node);
                      textArea.append(shortestPath + "\n");
                    }
                  }
                } else { // word1 和 word2 均不为空
                  String result = calcShortestPath(word1, word2);
                  textArea.append(result + "\n");
                }
            } else {
                textArea.append("please input at least one node name!" + "\n");
            }
        }
      }
    });

    //开启随机游走功能
    JButton randomWalkButton = new JButton("Start Random Walk");
    randomWalkButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          stopRequested.set(false);
          if (graph.isEmpty()) {
            textArea.append("Please press Load Text File button first!\n");
          } else {
              String result = randomWalk();
              System.out.println(result);
          }
        }
    });

    //停止随机游走
    JButton stoprandomWalkButton = new JButton("Pause Random Walk");
    stoprandomWalkButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            stopRequested.set(true);
        }
    });

    // 创建一个GridBagLayout布局的JPanel
    final JPanel buttonPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH; // 设置组件填充方式为填充整个单元格
    gbc.weightx = 1; // 设置组件水平拉伸权重
    gbc.weighty = 1; // 设置组件垂直拉伸权重
    gbc.ipadx = 10; // 设置组件内部填充的水平大小
    gbc.ipady = 10; // 设置组件内部填充的垂直大小
    gbc.gridx = 0; // 设置组件的起始列
    gbc.gridy = 0; // 设置组件的起始行
    gbc.gridx = 1;
    buttonPanel.add(loadButton, gbc);
    gbc.gridx++;
    buttonPanel.add(randomWalkButton, gbc);
    gbc.gridx++;
    buttonPanel.add(stoprandomWalkButton, gbc);
    gbc.gridy++;
    gbc.gridx = 0;
    buttonPanel.add(word1Field, gbc);
    gbc.gridx++;
    buttonPanel.add(word2Field, gbc);
    gbc.gridx++;
    buttonPanel.add(queryButton, gbc);
    gbc.gridx++;
    buttonPanel.add(shortestPathButton, gbc);
    gbc.gridy++;
    gbc.gridx = 0;
    buttonPanel.add(newTextField, gbc);
    gbc.gridx = 3;
    buttonPanel.add(generateButton, gbc);
    setLayout(new BorderLayout());
    add(new JScrollPane(textArea), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
    setTitle("Text Graph Processor");
    setSize(850, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
     * Processes the text file and builds the directed graph.
  */
  public void processTextFile(String filePath) {
    graph.clear(); // 清空之前的图
    // 验证并规范化文件路径
    //    Path file = Paths.get(filePath).normalize();
    Path file = Paths.get(filePath).toAbsolutePath().normalize(); // 使用绝对路径确保安全
    // 验证文件路径的安全性
    if (!Files.isRegularFile(file) || !Files.exists(file)) {
      System.err.println("Invalid file path or file does not exist.");
      return;
    }
    //try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
    try (BufferedReader reader = Files.newBufferedReader(file)) {
      String line;
      String previousWord = null;
      while ((line = reader.readLine()) != null) {
        String[] words = line.toLowerCase().split("\\s+");
        for (String word : words) {
          word = word.replaceAll("[^a-z]", ""); // 移除非字母字符
          System.out.println(word);
          if (!word.isEmpty()) {
            // 如果图中不存在该单词，则添加到图中
            graph.putIfAbsent(word, new HashMap<>());
            // 如果不是文本中的第一个单词，则需要添加边
            if (previousWord != null) {
              graph.get(previousWord).put(word, graph.get(previousWord).getOrDefault(word, 0) + 1);
            }
            previousWord = word;
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    printGraph();
  }

  /**
     * Prints the directed graph to the console.
  */
  public void printGraph() {
    if (graph.isEmpty()) {
      System.out.println("graph is null!");
    } else {
      for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
        String sourceWord = entry.getKey();
        Map<String, Integer> edges = entry.getValue();
        System.out.println("Source Word: " + sourceWord);
        for (Map.Entry<String, Integer> edge : edges.entrySet()) {
          String targetWord = edge.getKey();
          int weight = edge.getValue();
          System.out.println("  -> " + targetWord + " (" + weight + ")");
        }
      }
    }
  }



  /**
     * Queries bridge words between two given words.
  */
  public String queryBridgeWords(String word1, String word2) {
    if (!graph.containsKey(word1)) {
      if (graph.containsKey(word2)) {
        return "No \"" + word1 + "\" in the graph!";
      } else {
        return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
      }
    } else {
      if (!graph.containsKey(word2)) {
        return "No \"" + word2 + "\" in the graph!";
      }
    }
    List<String> bridgeWords = new ArrayList<>();
    for (String bridgeWord : graph.get(word1).keySet()) {
      if (graph.get(bridgeWord).containsKey(word2)) {
        bridgeWords.add(bridgeWord);
      }
    }
    if (bridgeWords.isEmpty()) {
      return "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
    } else {
      if (bridgeWords.size() == 1) {
        return "The bridge words from \"" + word1 + "\" to \"" + word2
                +
                "\" is: " + String.join(", ", bridgeWords);
      } else {
        return "The bridge words from \"" + word1 + "\" to \"" + word2
                +
                "\" are: " + String.join(", ", bridgeWords);
      }
    }
  }

  /**
  * Generates a new text by inserting bridge words between consecutive words in the input text.
  *
  * @param inputText The original input text.
  * @return The newly generated text with bridge words inserted.
  */
  public String generateNewText(String inputText) {
    String[] words = inputText.toLowerCase().split("\\s+");
    StringBuilder newText = new StringBuilder();
    //Random random = new Random();
    for (int i = 0; i < words.length - 1; i++) {
      String word1 = words[i];
      String word2 = words[i + 1];
      //检查图中是否包含这两个结点
      if (graph.containsKey(word1) && graph.containsKey(word2)) {
        //查询所有桥接词
        List<String> bridgeWords = new ArrayList<>();
        for (String bridgeWord : graph.get(word1).keySet()) {
          if (graph.get(bridgeWord).containsKey(word2)) {
            bridgeWords.add(bridgeWord);
          }
        }
        newText.append(word1).append(" ");
        //选择一个桥接词插入
        if (!bridgeWords.isEmpty()) {
          //int randomIndex = random.nextInt(bridgeWords.size());
          int randomIndex = secureRandom.nextInt(bridgeWords.size());
          newText.append(bridgeWords.get(randomIndex)).append(" ");
        }
      } else {
        newText.append(word1).append(" ");
      }
    }
    newText.append(words[words.length - 1]); //添加最后一个文本单词
    return newText.toString();
  }

  /**
   * Calculates the shortest path between two words in the graph and visualizes it.
   */
  public String calcShortestPath(String word1, String word2) {
    if (graph.containsKey(word1) && graph.containsKey(word2)) {
      List<String> paths = CalcShortestPaths.calcShortestPath(graph, word1, word2);
      System.out.println(paths);
      StringBuilder result = new StringBuilder();
      int i = 1;
      String tmp = flag ? word1 : word2;
      for (String input : paths) {
        String[] words = input.split("->");
        List<String> wordList = Arrays.asList(words); // 转换数组为列表
        GraphVisualizer.showshortest(wordList, i, tmp); // 调用时传递列表参数
        StringBuilder pathBuilder = new StringBuilder();
        boolean isFirst = true;
        for (String word : words) {
          if (!isFirst) {
            pathBuilder.append(" -> ");
          } else {
            isFirst = false;
          }
          pathBuilder.append(word);
        }
        String path = pathBuilder.toString();
        String shortestPathLength = String.valueOf(words.length - 1);
        result.append("The shortest path from \"").append(word1).append("\" to \"").append(word2)
              .append("\" is: ").append(path)
              .append("\n").append("The shortest path's "
                        +
                        "length is ").append(shortestPathLength).append("\n");
        i++;
      }
      if (result.length() == 0) {
        result.append("cannot reach!\n");
      }
      return result.toString();
    } else {
      return "node does not exist！";
    }
  }

  /**
   * Initiates a random walk in the graph, starting from
   * a random node and writes the walk result to a file.
   */
  public String randomWalk() {
    Thread thread =  new Thread(new Runnable() {
        @Override
        public void run() {
            //随机选择一个起始节点
            List<String> nodes = new ArrayList<>(graph.keySet());
            Collections.shuffle(nodes);
            String currentNode = nodes.get(0);

            //初始化路径
            List<String> walk = new ArrayList<>();
            Set<String> visitedEdges = new HashSet<>();
            //Random random = new Random();
            textArea.setText("");
            textArea.append(currentNode);
            //开始随机游走
            while (!stopRequested.get()) {
            // 每一步延迟1ms
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                int random = secureRandom.nextInt(1000); // bound为实际需要的上限
                SwingUtilities.invokeLater(() -> textArea.append("Random walk interrupted."));
            }
            walk.add(currentNode);
            //检查是否有出边
            Map<String, Integer> outgoingEdges = graph.get(currentNode);
            if (outgoingEdges.isEmpty() || stopRequested.get()) {
                break; //没有出边则停止随机游走
            }

            //随机选择一条边
            List<String> edges = new ArrayList<>(outgoingEdges.keySet());
            Collections.shuffle(edges);
            String nextNode = edges.get(0);
            SwingUtilities.invokeLater(() -> textArea.append(" -> " + nextNode));
            //检查是否重复访问过该边
            String edge = currentNode + " -> " + nextNode;
            if (visitedEdges.contains(edge)) {
                walk.add(nextNode);
                break; //若重复访问过则停止
            }
            //标记并游走到下一个结点
            visitedEdges.add(edge);
            currentNode = nextNode;

            }
            String walkText = String.join(" ", walk);
            //将随机游走结果写回到磁盘文件
            try {
              writeWalkToFile(walkText);
            } catch (IOException e) {
              SwingUtilities.invokeLater(() ->
                      textArea.append("Error writing walk to file: " + e.getMessage()));
            }
            SwingUtilities.invokeLater(() ->
                    textArea.append("\nRandom walk result written to file.\n"));
        }
    });
    thread.start();
    return "Random walk is done!\n"; // 返回结果
  }

  /**
   * Writes the random walk result to a file.
   *
   * @param walkText The text to be written to the file.
   * @throws IOException If an I/O error occurs.
   */
  public void writeWalkToFile(String walkText) throws IOException {
    String fileName = "random_walk_result.txt";
    //    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
    //      writer.write(walkText);
    //    }
    Path filePath = Paths.get(fileName);
    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
      writer.write(walkText);
    }
  }
}