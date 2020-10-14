package com.company;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class    TraverseJson {
    ArrayList<String> getValuesForGivenKey(List<String> jsonList, String key) throws JSONException {
        JSONArray jsonArray;
        ArrayList<String> jsonValueList = new ArrayList<>();
        for (String jsonArrayStr: jsonList) {
                jsonArrayStr = jsonArrayStr.substring(jsonArrayStr.indexOf("["), jsonArrayStr.indexOf("]") + 1);
                jsonArray = new JSONArray(jsonArrayStr);
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    if (!jsonObject.optString(key).equals(""))
                        jsonValueList.add(jsonObject.optString(key));
                }
        }
        Collections.sort(jsonValueList);
        return jsonValueList;
    }

    ArrayList<String> getJsonForGivenValue(List<String> jsonList, String key, String value) throws JSONException{
        JSONArray jsonArray;
        ArrayList<String> jsonValueList = new ArrayList<>();
        boolean hasAsterisks = value.endsWith("*");
        if (hasAsterisks) {
            value = value.substring(0, value.length() - 1);
        }
        for (String jsonArrayStr : jsonList) {
                jsonArrayStr = jsonArrayStr.substring(jsonArrayStr.indexOf("["), jsonArrayStr.indexOf("]") + 1);
                jsonArray = new JSONArray(jsonArrayStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (hasAsterisks) {
                        if (jsonObject.optString(key).startsWith(value)) {
                            jsonValueList.add(jsonObject.toString());
                        }
                    } else {
                        if (jsonObject.optString(key).equals(value)) {
                            jsonValueList.add(jsonObject.toString());
                        }
                    }
                }
        }
        Collections.sort(jsonValueList);
        return jsonValueList;
    }
}
