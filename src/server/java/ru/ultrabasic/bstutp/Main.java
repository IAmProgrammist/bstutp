package ru.ultrabasic.bstutp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.desktop.SystemSleepEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<String> list = new ArrayList<String>();
        list.add("Raja");
        list.add("Jai");
        list.add("Adithya");
        JSONArray array = new JSONArray();
        for(int i = 0; i < list.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put(String.valueOf(i), i);
            array.put(obj);
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("Employee Names:", array);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        System.out.println(obj.toString());
    }
}
