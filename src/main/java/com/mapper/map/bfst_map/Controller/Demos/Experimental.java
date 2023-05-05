package com.mapper.map.bfst_map.Controller.Demos;

import com.mapper.map.bfst_map.Model.Elements.Model;

public class Experimental {


    static String filename = "data/kbh.osm";
    static Model model;

    public static void main(String[] args) {
        //createModel();

    }

    public static void createModel() {
        double timerStart = System.currentTimeMillis();

        try {
            model = Model.load(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double timerFinish = System.currentTimeMillis();
        System.out.println("================= Time to load model: " + (timerFinish - timerStart) + "ms =================");

    }






}
