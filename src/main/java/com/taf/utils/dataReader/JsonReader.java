package com.taf.utils.dataReader;

import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.taf.utils.logs.LogManager;

import java.io.FileReader;

public class JsonReader {

    String jsonReader;
    String jsonFileName;

    private final String test_data_path = "src/main/resources/test-data/";

    public JsonReader(String jsonFileName) {
        this.jsonFileName = jsonFileName;
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(test_data_path + jsonFileName+".json"));
            jsonReader = data.toJSONString();
        } catch (Exception e) {
            LogManager.error("Error reading json file: ", test_data_path , jsonFileName,".json\n" , e.getMessage());
        }
    }

    public String getJsonData(String jsonPropertyName) {
        try {
            return JsonPath.read(jsonReader, jsonPropertyName);
        } catch (Exception e) {
            LogManager.error("Error reading json data with path: " , jsonPropertyName , "\n" , e.getMessage());
            return "";
        }
    }

}
