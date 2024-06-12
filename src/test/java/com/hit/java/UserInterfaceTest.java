package com.hit.java;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserInterfaceTest {

    private UserInterface userInterface;

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void calcShortestPath() {
    }

    @BeforeEach
    void setUp() throws IOException {
        userInterface = new UserInterface();
        userInterface.processTextFile("C:\\Users\\25631\\Desktop\\test.txt");
    }

    @Test
    public void testCalcShortestPath_ValidNodes() {
        String result = userInterface.calcShortestPath("to", "the");
        assertEquals("The shortest path from \"to\" to \"the\" is: to -> catch -> the\nThe shortest path's length is 2\n", result);
    }

    @Test
    public void testCalcShortestPath_Word2Empty() throws IOException {
        String[] words = {"over", "rest", "quick", "away", "lazy", "quickly", "decides", "tree", "jumps", "barks", "feeling", "brown", "fox", "the", "chases", "at", "same", "safe", "catch", "under", "dog", "runs"};
        String[] expectedPaths = {
                "The shortest path from \"to\" to \"over\" is: to -> catch -> the -> quick -> brown -> fox -> jumps -> over\nThe shortest path's length is 7\n",
                "The shortest path from \"to\" to \"rest\" is: to -> rest\nThe shortest path's length is 1\n",
                "The shortest path from \"to\" to \"quick\" is: to -> catch -> the -> quick\nThe shortest path's length is 3\n",
                "The shortest path from \"to\" to \"away\" is: to -> catch -> the -> quick -> brown -> fox -> runs -> away\nThe shortest path's length is 7\n",
                "The shortest path from \"to\" to \"lazy\" is: to -> catch -> the -> lazy\nThe shortest path's length is 3\n",
                "The shortest path from \"to\" to \"quickly\" is: to -> catch -> the -> quick -> brown -> fox -> runs -> away -> quickly\nThe shortest path's length is 8\n",
                "The shortest path from \"to\" to \"decides\" is: to -> catch -> the -> lazy -> dog -> decides\nThe shortest path's length is 5\n",
                "The shortest path from \"to\" to \"tree\" is: to -> catch -> the -> tree\nThe shortest path's length is 3\n",
                "The shortest path from \"to\" to \"jumps\" is: to -> catch -> the -> quick -> brown -> fox -> jumps\nThe shortest path's length is 6\n",
                "The shortest path from \"to\" to \"barks\" is: to -> catch -> the -> lazy -> dog -> barks\nThe shortest path's length is 5\n",
                "The shortest path from \"to\" to \"feeling\" is: to -> catch -> the -> tree -> feeling\nThe shortest path's length is 4\n",
                "The shortest path from \"to\" to \"brown\" is: to -> catch -> the -> quick -> brown\nThe shortest path's length is 4\n",
                "The shortest path from \"to\" to \"fox\" is: to -> catch -> the -> quick -> brown -> fox\nThe shortest path's length is 5\n",
                "The shortest path from \"to\" to \"the\" is: to -> catch -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"to\" to \"chases\" is: to -> catch -> the -> lazy -> dog -> chases\nThe shortest path's length is 5\n",
                "The shortest path from \"to\" to \"at\" is: to -> catch -> the -> lazy -> dog -> barks -> at\nThe shortest path's length is 6\n",
                "The shortest path from \"to\" to \"same\" is: to -> catch -> the -> quick -> same\nThe shortest path's length is 4\n",
                "The shortest path from \"to\" to \"safe\" is: to -> catch -> the -> tree -> feeling -> safe\nThe shortest path's length is 5\n",
                "The shortest path from \"to\" to \"catch\" is: to -> catch\nThe shortest path's length is 1\n",
                "The shortest path from \"to\" to \"under\" is: to -> rest -> under\nThe shortest path's length is 2\n"+
                        "The shortest path from \"to\" to \"under\" is: to -> catch -> under\nThe shortest path's length is 2\n",
                "The shortest path from \"to\" to \"dog\" is: to -> catch -> the -> lazy -> dog\nThe shortest path's length is 4\n",
                "The shortest path from \"to\" to \"runs\" is: to -> catch -> the -> quick -> brown -> fox -> runs\nThe shortest path's length is 6\n"
        };

        for (int i = 0; i < words.length; i++) {
            String result = userInterface.calcShortestPath("to", words[i]);
            assertEquals(expectedPaths[i], result, "Failed for word: " + words[i]);
        }
    }

    @Test
    public void testCalcShortestPath_Word1Empty() throws IOException {
        String[] words = {"over", "rest", "quick", "away", "lazy", "quickly", "decides", "tree", "jumps", "barks", "feeling", "brown", "fox", "chases", "at", "same", "safe", "to", "catch", "under", "dog", "runs"};
        String[] expectedPaths = {
                "The shortest path from \"over\" to \"the\" is: over -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"rest\" to \"the\" is: rest -> under -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"quick\" to \"the\" is: quick -> same -> tree -> the\nThe shortest path's length is 3\n",
                "The shortest path from \"away\" to \"the\" is: away -> quickly -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"lazy\" to \"the\" is: lazy -> dog -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"quickly\" to \"the\" is: quickly -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"decides\" to \"the\" is: decides -> to -> catch -> the\nThe shortest path's length is 3\n",
                "The shortest path from \"tree\" to \"the\" is: tree -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"jumps\" to \"the\" is: jumps -> over -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"barks\" to \"the\" is: barks -> at -> the\nThe shortest path's length is 2\n",
                "cannot reach!\n",
                "The shortest path from \"brown\" to \"the\" is: brown -> fox -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"fox\" to \"the\" is: fox -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"chases\" to \"the\" is: chases -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"at\" to \"the\" is: at -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"same\" to \"the\" is: same -> tree -> the\nThe shortest path's length is 2\n",
                "cannot reach!\n",
                "The shortest path from \"to\" to \"the\" is: to -> catch -> the\nThe shortest path's length is 2\n",
                "The shortest path from \"catch\" to \"the\" is: catch -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"under\" to \"the\" is: under -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"dog\" to \"the\" is: dog -> the\nThe shortest path's length is 1\n",
                "The shortest path from \"runs\" to \"the\" is: runs -> away -> quickly -> the\nThe shortest path's length is 3\n"
        };

        for (int i = 0; i < words.length; i++) {
            String result = userInterface.calcShortestPath(words[i], "the");
            assertEquals(expectedPaths[i], result, "Failed for word: " + words[i]);
        }
    }

    @Test
    public void testCalcShortestPath_BothEmpty() {
        String result = userInterface.calcShortestPath("", "");
        assertEquals("node does not exist！", result);
    }

    @Test
    public void testCalcShortestPath_Word2NotExist() {
        String result = userInterface.calcShortestPath("to", "turtle");
        assertEquals("node does not exist！", result);
    }

    @Test
    public void testCalcShortestPath_Word1NotExist() {
        String result = userInterface.calcShortestPath("six", "to");
        assertEquals("node does not exist！", result);
    }

    @Test
    public void testCalcShortestPath_BothWordsNotExist() {
        String result = userInterface.calcShortestPath("six", "turtle");
        assertEquals("node does not exist！", result);
    }


}