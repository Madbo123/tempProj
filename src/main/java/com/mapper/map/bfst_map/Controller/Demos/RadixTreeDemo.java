package com.mapper.map.bfst_map.Controller.Demos;

import com.mapper.map.bfst_map.Controller.AddressSearcher.RadixTree;
import com.mapper.map.bfst_map.Controller.AddressSearcher.TernarySearchTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RadixTreeDemo {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            File file = new File("./data/Test.txt");
            Scanner scan = new Scanner(file);

            RadixTree<String> radixTree = new RadixTree<>();
            while (scan.hasNextLine()) {
                String string = scan.nextLine();
                radixTree.put(string, string);
            }

            for (String string : radixTree.keysWithPrefix("Teste", 0)) {
                System.out.println(string);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}