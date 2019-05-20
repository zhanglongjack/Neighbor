package com.neighbor.app.pay.common;

import java.util.Comparator;
import java.util.Map;

public class MapComparator implements Comparator<Map.Entry<String, String>> {
    @Override
    public int compare(Map.Entry<String, String> arg0, Map.Entry<String, String> arg1) {
        return (arg0.getKey()).compareTo(arg1.getKey());
    }
}
