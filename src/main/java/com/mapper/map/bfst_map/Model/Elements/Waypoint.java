package com.mapper.map.bfst_map.Model.Elements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Waypoint implements Serializable {
    public String address;
    protected String streetName, streetNumber, postCode, city, country;
    protected Map<String, String> tags;

    //Approx center coords for camera movement
    private float[] center;

    public Waypoint(Map<String, String> tags) {
        this.tags = new HashMap<>(tags);

        if (tags.containsKey("highway")) {
            streetName = tags.get("name");
            address = streetName;
        } else {
            streetName = tags.get("addr:street");
            streetNumber = tags.get("addr:housenumber");
            postCode = tags.get("addr:postcode");
            address = streetName + " " + streetNumber + ", " + postCode;
        }

        try {
            if (address != null) {
                Model.addressTST.put(address, this);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String getAddress() {
        return address;
    }

    public float[] getCenter() {
        return center;
    }

    protected void updateAddr(String fieldName, String newString) {
        assert fieldName.length() > 0 && newString.length() > 0 : "Empty String input not allowed";

        switch (fieldName) {
            case "streetName" -> streetName = newString;
            case "streetNumber" -> streetNumber = newString;
            case "postcode" -> postCode = newString;
            case "city" -> city = newString;
            case "country" -> country = newString;
            default -> throw new NoSuchElementException("Field name incorrect, no match found");
        }
    }

    public boolean containsTag(String tag) {
        return this.tags.containsKey(tag) | this.tags.containsValue(tag);
    }
}