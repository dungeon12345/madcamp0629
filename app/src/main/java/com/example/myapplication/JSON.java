package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSON {

    public JSON(){

    }

    public static JSONObject getJsonObject(ArrayList<ContactItem> contactItems){

        JSONObject JsonObject = new JSONObject();
        int len = contactItems.size();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("User_phNumber", contactItems.get(i).getUser_phNumber());
                sObject.put("User_name", contactItems.get(i).getUser_Name());
                System.out.println(sObject.toString());
                JsonObject = sObject;
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return JsonObject;
    }
}
