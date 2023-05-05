package com.mapper.map.bfst_map.Controller.Demos;

import com.mapper.map.bfst_map.Controller.AddressSearcher.TernarySearchTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TernarySearchTreeDemo {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./data/words.txt");
        Scanner scan = new Scanner(file);

        TernarySearchTree<Integer> ternarySearchTree = new TernarySearchTree<>();
        int i = 0;
        while (scan.hasNextLine()) {
            ternarySearchTree.put(scan.nextLine(), i);
            i++;
        }

        for (String s : ternarySearchTree.keysWithPrefix("defene", 3)) {
            System.out.println(s);
        }
    }
}