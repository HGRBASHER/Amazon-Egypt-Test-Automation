package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class JSONFileManager {
            public LinkedHashMap<String, Object> jsonData;

    public JSONFileManager(String path) {
        try {
            jsonData=new Gson().fromJson(new FileReader(path), new TypeToken<LinkedHashMap<String,Object>>() {}.getType());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public Object getValue(String keyPath) {
        String[] keys = keyPath.split("\\.");
        Object current = jsonData;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                return null;
            }
        }
        return current;
    }
    public ArrayList<String> getListValue(String key){
        return  (ArrayList<String>) jsonData.get(key);
    }
}
