package com.mapper.map.bfst_map.Controller.AddressSearcher;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class TernarySearchTreeTest {
    private TernarySearchTree<Integer> TST;

    @BeforeEach
    public void setUp() {
        TST = new TernarySearchTree<>();
        TST.put("pasta", 0);
        TST.put("pangless", 1);
        TST.put("polster", 2);
        TST.put("sasins", 3);
        TST.put("banana", 4);
        TST.put("pasta", 5);
        TST.put("bantered", 6);
        TST.put("banter", 7);
        TST.put("bested", 8);
        TST.put("busted", 9);
        TST.put("bets", 10);
        TST.put("sasins", null);
    }

    @Test
    public void putWithNormal() {
        String message = "";

        try {
            TST.put("hello", 11);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("", message);
    }

    @Test
    public void putWithNull() {
        String message = "";

        try {
            TST.put(null, 11);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling put(String key) with null string.", message);
    }

    @Test
    public void putWithEmpty() {
        String message = "";

        try {
            TST.put("", 11);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling put(String key) with empty string.", message);
    }

    @Test
    public void getWithNormal() {
        assertEquals(2, TST.get("polster"));
    }

    @Test
    public void getWithOverwritten() {
        assertEquals(5, TST.get("pasta"));
    }

    @Test
    public void getWithDeleted() {
        assertNull(TST.get("sasins"));
    }

    @Test
    public void getWithWrong() {
        assertNull(TST.get("random"));
    }

    @Test
    public void getWithNull() {
        String message = "";

        try {
            TST.get(null);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling get(String key) with null string.", message);
    }

    @Test
    public void getWithEmpty() {
        String message = "";

        try {
            TST.get("");
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling get(String key) with empty string.", message);
    }

    @Test
    public void containsWithNormal() {
        assertTrue(TST.contains("polster"));
    }

    @Test
    public void containsWithOverwritten() {
        assertTrue(TST.contains("pasta"));
    }

    @Test
    public void containsWithDeleted() {
        assertFalse(TST.contains("sasins"));
    }

    @Test
    public void containsWithWrong() {
        assertFalse(TST.contains("random"));
    }

    @Test
    public void containsWithNull() {
        String message = "";

        try {
            TST.contains(null);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling contains(String key) with null string.", message);
    }

    @Test
    public void containsWithEmpty() {
        String message = "";

        try {
            TST.contains("");
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling contains(String key) with empty string.", message);
    }

    @Test
    public void sizeWithNine() {
        assertEquals(9, TST.size());
    }

    @Test
    public void sizeAfterPut() {
        TST.put("hello", 11);

        assertEquals(10, TST.size());
    }

    @Test
    public void sizeAfterOverwrite() {
        TST.put("banter", 11);

        assertEquals(9, TST.size());
    }

    @Test
    public void sizeAfterDeletion() {
        TST.put("pasta", null);

        assertEquals(8, TST.size());
    }

    @Test
    public void keys() {
        List<String> keys = new ArrayList<>();
        for (String key : TST.keys(0)) {
            keys.add(key);
        }

        assertEquals("banana", keys.get(0));
        assertEquals("banter", keys.get(1));
        assertEquals("bantered", keys.get(2));
        assertEquals("bested", keys.get(3));
        assertEquals("bets", keys.get(4));
        assertEquals("busted", keys.get(5));
        assertEquals("pangless", keys.get(6));
        assertEquals("pasta", keys.get(7));
        assertEquals("polster", keys.get(8));
    }

    @Test
    public void keysWithEmptyTree() {
        TST.put("pasta", null);
        TST.put("pangless", null);
        TST.put("polster", null);
        TST.put("banana", null);
        TST.put("bantered", null);
        TST.put("banter", null);
        TST.put("bested", null);
        TST.put("busted", null);
        TST.put("bets", null);

        List<String> keys = new ArrayList<>();
        for (String key : TST.keys(0)) {
            keys.add(key);
        }

        assertEquals(0, keys.size());
    }

    @Test
    public void keysWithPrefixWithOneLetter() {
        List<String> keys = new ArrayList<>();
        for (String key : TST.keysWithPrefix("b", 0)) {
            keys.add(key);
        }

        assertEquals("banana", keys.get(0));
        assertEquals("banter", keys.get(1));
        assertEquals("bantered", keys.get(2));
        assertEquals("bested", keys.get(3));
        assertEquals("bets", keys.get(4));
        assertEquals("busted", keys.get(5));
    }

    @Test
    public void keysWithPrefixWithTwoLetters() {
        List<String> keys = new ArrayList<>();
        for (String key : TST.keysWithPrefix("be", 0)) {
            keys.add(key);
        }

        assertEquals("bested", keys.get(0));
        assertEquals("bets", keys.get(1));
    }

    @Test
    public void keysWithPrefixWithThreeLetters() {
        List<String> keys = new ArrayList<>();
        for (String key : TST.keysWithPrefix("bes", 0)) {
            keys.add(key);
        }

        assertEquals("bested", keys.get(0));
    }

    @Test
    public void keysWithPrefixWithWrong() {
        List<String> keys = new ArrayList<>();
        for (String key : TST.keysWithPrefix("hello", 0)) {
            keys.add(key);
        }

        assertEquals(0, keys.size());
    }

    @Test
    public void keysWithPrefixWithNull() {
        String message = "";

        try {
            TST.keysWithPrefix(null, 0);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling keysWithPrefix(String prefix) with null string.", message);
    }

    @Test
    public void keysWithPrefixWithEmpty() {
        String message = "";

        try {
            TST.keysWithPrefix("", 0);
        } catch(Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling keysWithPrefix(String prefix) with empty string.", message);
    }
}