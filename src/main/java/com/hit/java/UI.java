package com.hit.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


import static com.hit.java.GraphVisualizer.showDirectedGraph;

public class UI extends JFrame {
    private Map<String, Map<String, Integer>> graph;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private JTextField word1Field;
    private JTextField word2Field;
    private JTextField newTextField;
    private AtomicBoolean stopRequested = new AtomicBoolean(false);

    private  boolean flag = true;

    //主入口函数
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UI();
            }
        });
    }

    //展示界面
    public UI() {
        graph = new HashMap<>();
        textArea = new JTextArea();
        textArea.setEditable(false);
        fileChooser = new JFileChooser();
        //读入文本生成有向图
        JButton loadButton = new JButton("Load Text File");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(UI.this);
                //检查用户是否“打开”按钮
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    processTextFile(file.getAbsolutePath());
                    showDirectedGraph(graph);
                }
            }
        });

        //两个文本的输入框
        word1Field = new JTextField(10);
        word2Field = new JTextField(10);
        //查询桥接词
        JButton queryButton = new JButton("Query Bridge Words");
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(graph == null || graph.isEmpty()){
                    textArea.append("please press load Text File button first\n");
                }
                else{
                    String word1 = word1Field.getText();
                    String word2 = word2Field.getText();
                    String result = queryBridgeWords(word1, word2);
                    System.out.println(result);
                    textArea.append(result + "\n");
                }

            }
        });

        newTextField = new JTextField(20);
        //生成新文本
        JButton generateButton = new JButton("Generate New Text");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(graph == null || graph.isEmpty()){
                    textArea.append("please press load Text File button first\n");
                }
                else{
                    String inputText = newTextField.getText();
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
                if(graph == null || graph.isEmpty()){
                    textArea.append("please press load Text File button first\n");
                }
                else {
                    String word1 = word1Field.getText();
                    String word2 = word2Field.getText();
                    //flag用于标记which输入框为空
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
                    } else if (word2 != null && word1 != null) {
                        String result = calcShortestPath(word1, word2);
                        textArea.append(result + "\n");
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
                if(graph == null || graph.isEmpty()){
                    textArea.append("please press load Text File button first\n");
                }
                else {
                    stopRequested.set(false);
                    String result = randomWalk();
                }
            }
        });

        //停止随机游走按钮
        JButton StoprandomWalkButton = new JButton("Pause Random Walk");
        StoprandomWalkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRequested.set(true);
            }
        });


        //GUI（图形用户界面）开发框架是 Swing
        // 创建一个GridBagLayout布局的JPanel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // 设置组件填充方式为填充整个单元格
        gbc.weightx = 1; // 设置组件水平拉伸权重
        gbc.weighty = 1; // 设置组件垂直拉伸权重
        gbc.ipadx = 10; // 设置组件内部填充的水平大小
        gbc.ipady = 10; // 设置组件内部填充的垂直大小
        gbc.gridy = 0; // 设置组件的起始行
        gbc.gridx = 1;
        buttonPanel.add(loadButton, gbc);
        gbc.gridx++;
        buttonPanel.add(randomWalkButton, gbc);
        gbc.gridx++;
        buttonPanel.add(StoprandomWalkButton, gbc);
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


    public void processTextFile(String filePath) {
        graph.clear(); // 清空之前的图
        //读入缓冲区
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String previousWord = null;
            while ((line = reader.readLine()) != null) {
                //将读取到的每一行文本转换为小写，并使用空格作为分隔符将文本拆分成单词数组。
                String[] words = line.toLowerCase().split("\\s+");
                for (String word : words) {
                    //将非a-z字母字符删除
                    word = word.replaceAll("[^a-z]", ""); // 移除非字母字符
                    System.out.println(word);
                    if (!word.isEmpty()) {
                        // 如果图中不存在该单词，则添加到图中
                        graph.putIfAbsent(word, new HashMap<>());
                        // 如果不是文本中的的单词，则更新图
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
        //控制打印图信息
        printGraph();

    }

    //控制台打印
    public void printGraph() {
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

    public String queryBridgeWords(String word1, String word2) {
        if(!graph.containsKey(word1) && graph.containsKey(word2)){
            return "No \""+ word1 + "\" in the graph!";
        }
        else if(!graph.containsKey(word2) && graph.containsKey(word1)){
            return "No \""+ word2 + "\" in the graph!";
        }
        else if(!graph.containsKey(word1) && !graph.containsKey(word2)){
            return "No \""+ word1 + "\" and \"" + word2 + "\" in the graph!";
        }


        List<String> bridgeWords = new ArrayList<>();
        for (String bridgeWord : graph.get(word1).keySet()) {
            if (graph.get(bridgeWord).containsKey(word2)) {
                bridgeWords.add(bridgeWord);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from \""+ word1 + "\" to \"" + word2 + "\"!";
        } else {
            if(bridgeWords.size()==1){
                return "The bridge words from \""+ word1 + "\" to \"" + word2 + "\" is: " + String.join(", ", bridgeWords);
            }
            else{
                return "The bridge words from \""+ word1 + "\" to \"" + word2 + "\" are: " + String.join(", ", bridgeWords);
            }
        }
    }

    public String generateNewText(String inputText) {
        String[] words = inputText.toLowerCase().split("\\s+");
        StringBuilder newText = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];

            // 检查word1和word2是否在graph中
            if (graph.containsKey(word1) && graph.containsKey(word2)) {
                // Find all bridge words from word1 to word2
                List<String> bridgeWords = new ArrayList<>();
                for (String bridgeWord : graph.get(word1).keySet()) {
                    if (graph.get(bridgeWord).containsKey(word2)) {
                        bridgeWords.add(bridgeWord);
                    }
                }
                newText.append(word1).append(" ");
                //如果有桥接词，插入
                if (!bridgeWords.isEmpty()) {
                    int randomIndex = random.nextInt(bridgeWords.size());
                    newText.append(bridgeWords.get(randomIndex)).append(" ");
                }
            } else {
                newText.append(word1).append(" ");
            }
        }

        newText.append(words[words.length - 1]); // 添加最后一个词

        return newText.toString();
    }



    public String calcShortestPath(String word1, String word2) {
        if (graph.containsKey(word1) && graph.containsKey(word2)) {
            List<String> paths = calshortestpaths.calcShortestPath(graph, word1, word2);
            System.out.println(paths);
            List<List<String>> wordLists = new ArrayList<>();

            for (String input : paths) {
                // 使用箭头"->"作为分隔符分割字符串
                String[] words = input.split("->");
                // 将分割后的单词数组转换为列表
                List<String> wordList = new ArrayList<>(Arrays.asList(words));
                wordLists.add(wordList);
            }

            //生成展示的图片名称
            String res=null;
            Integer i=1;
            String tmp;
            if(flag)  tmp=word1;
            else tmp=word2;

            //打印所有最短路径
            for (List<String> singlepath : wordLists) {
                GraphVisualizer.showshortest(singlepath,i,tmp);
                String shortestPathLength = String.valueOf(singlepath.size()-1);
                res= res + "The shortest path from \"" + word1 + "\" to \"" + word2 + "\" is: " + String.join(" -> ", singlepath) + "\n"+ "The shortest path's length is "+shortestPathLength+"\n";
                i++;
            }
            if(res == null ) res="cannot reach!";
            return res;
        }
        else{
            String tip = "node does not exist！";
            return tip;
        }
    }

    //随机游走
    public String randomWalk() {
        Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                // 选择一个起始节点
                List<String> nodes = new ArrayList<>(graph.keySet());
                Collections.shuffle(nodes);
                String currentNode = nodes.get(0);

                // 初始化路径
                List<String> walk = new ArrayList<>();
                Set<String> visitedEdges = new HashSet<>();
                textArea.append(currentNode);
                // 执行随机游走
                while (!stopRequested.get()) {
                    // Delay for 1 second after each node visit
                    try {
                        Thread.sleep(1000); // 延时1s
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        SwingUtilities.invokeLater(() -> textArea.append("Random walk interrupted."));
                    }
                    walk.add(currentNode);
                    // 获得当前节点的邻接边
                    Map<String, Integer> outgoingEdges = graph.get(currentNode);
                    if (outgoingEdges.isEmpty() || stopRequested.get()) {
                        break; //没有出边结束遍历
                    }

                    // 随机选择一个边
                    List<String> edges = new ArrayList<>(outgoingEdges.keySet());
                    //随机打乱列表中元素顺序
                    Collections.shuffle(edges);
                    String nextNode = edges.get(0);
                    SwingUtilities.invokeLater(() -> textArea.append(" -> " + nextNode));
                    // 检查是否之前访问过该边
                    String edge = currentNode + " -> " + nextNode;

                    if (visitedEdges.contains(edge)) {
                        walk.add(nextNode);
                        break; // 检测到重复边，结束
                    }
                    // 标记下一个节点
                    visitedEdges.add(edge);
                    currentNode = nextNode;

                }
                String walkText = String.join(" ", walk);
                try {
                    writeWalkToFile(walkText);
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> textArea.append("Error writing walk to file: " + e.getMessage()));

                }
                SwingUtilities.invokeLater(() -> textArea.append("\nRandom walk result written to file.\n"));
            }
        });
        thread.start();
        return "Random walk is done!\n"; // 返回结果
    }

    public void writeWalkToFile(String walkText) throws IOException {
        String fileName = "random_walk_result.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(walkText);
        }
    }











}