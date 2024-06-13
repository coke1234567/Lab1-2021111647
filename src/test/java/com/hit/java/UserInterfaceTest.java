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

    @BeforeEach
    void setUp() throws IOException {
        userInterface = new UserInterface();
        userInterface.processTextFile("C:\\Users\\25631\\Desktop\\test.txt");
    }
    @Test
    public void whiteTest1() {
        String result = userInterface.queryBridgeWords("hello", "world");
        assertEquals("No \"hello\" and \"world\" in the graph!", result);
    }
    @Test
    public void whiteTest2() {
        String result = userInterface.queryBridgeWords("to", "world");
        assertEquals("No \"world\" in the graph!", result);
    }
    @Test
    public void whiteTest3() {
        String result = userInterface.queryBridgeWords("to", "fox");
        assertEquals("No bridge words from \"to\" to \"fox\"!", result);
    }
    @Test
    public void whiteTest4() {
        String result = userInterface.queryBridgeWords("rest", "catch");
        assertEquals("The bridge words from \"rest\" to \"catch\" is: under", result);
    }
    @Test
    public void whiteTest5() {
        String result = userInterface.queryBridgeWords("to", "under");
        assertEquals("The bridge words from \"to\" to \"under\" are: rest, catch", result);
    }
    @Test
    public void whiteTest6() {
        String result = userInterface.queryBridgeWords("world", "to");
        assertEquals("No \"world\" in the graph!", result);
    }
}