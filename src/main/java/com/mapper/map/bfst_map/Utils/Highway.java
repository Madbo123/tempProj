package com.mapper.map.bfst_map.Utils;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Highway {
    private static Map<String, Integer> roadTypeToSpeed;
    private static Map<String, Set<String>> roadTypeToTraversalTypes;
    private static Map<String, Color> roadTypeToColor;
    private static Map<String, Integer> roadTypeToSize;


    public static int getSpeed(String roadType) {
        if (roadType == null) {
            throw new IllegalArgumentException("Calling getSpeed(String roadType) with null string.");
        }

        if (roadType.length() == 0) {
            throw new IllegalArgumentException("Calling getSpeed(String roadType) with empty string.");
        }

        if (!roadTypeToSpeed.containsKey(roadType)) {
            return 50;
        }

        return roadTypeToSpeed.get(roadType);
    }

    public static Set<String> getTraversalTypes(String roadType) {
        if (roadType == null) {
            throw new IllegalArgumentException("Calling getTraversalTypes(String roadType) with null string.");
        }

        if (roadType.length() == 0) {
            throw new IllegalArgumentException("Calling getTraversalTypes(String roadType) with empty string.");
        }

        if (!roadTypeToTraversalTypes.containsKey(roadType)) {
            return new HashSet<>();
        }

        return roadTypeToTraversalTypes.get(roadType);
    }

    public static Color getColor(String roadType) {
        if (roadType == null) {
            throw new IllegalArgumentException("Calling getTraversalTypes(String roadType) with null string.");
        }

        if (roadType.length() == 0) {
            throw new IllegalArgumentException("Calling getTraversalTypes(String roadType) with empty string.");
        }

        if (!roadTypeToColor.containsKey(roadType)) {
            return Color.WHITE;
        }

        return roadTypeToColor.get(roadType);
    }

    public static int getSize(String roadType) {
        if (roadType == null) {
            throw new IllegalArgumentException("Calling getTraversalTypes(String roadType) with null string.");
        }

        if (roadType.length() == 0) {
            throw new IllegalArgumentException("Calling getTraversalTypes(String roadType) with empty string.");
        }

        if (!roadTypeToSize.containsKey(roadType)) {
            return 1;
        }

        return roadTypeToSize.get(roadType);
    }

    public static void loadData() {
        roadTypeToSpeed = new HashMap<>();
        roadTypeToTraversalTypes = new HashMap<>();
        roadTypeToColor = new HashMap<>();
        roadTypeToSize = new HashMap<>();

        try {
            File file = new File("./data/highway.txt");
            Scanner scan = new Scanner(file);

            List<String> data = new ArrayList<>();
            while (scan.hasNextLine()) {
                data.add(scan.nextLine());
            }

            for (int i = 0; i < data.size(); i += 6) {
                String name = data.get(i);
                int speed = Integer.parseInt(data.get(i + 1));
                String[] traversalTypes = data.get(i + 2).split(" ");
                Set<String> traversalSet = new HashSet<>(Arrays.asList(traversalTypes));
                String[] colors = data.get(i + 3).split(" ");
                Color color = new Color(Double.parseDouble(colors[0]), Double.parseDouble(colors[1]), Double.parseDouble(colors[2]), 1);
                int size = Integer.parseInt(data.get(i + 4));

                roadTypeToSpeed.put(name, speed);
                roadTypeToTraversalTypes.put(name, traversalSet);
                roadTypeToColor.put(name, color);
                roadTypeToSize.put(name, size);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}