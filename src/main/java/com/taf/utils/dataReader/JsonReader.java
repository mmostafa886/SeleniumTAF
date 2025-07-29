package com.taf.utils.dataReader;

import com.jayway.jsonpath.JsonPath;
import com.taf.utils.logs.LogsManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonReader {

    String jsonReader;
    String jsonFileName;

    private final String Test_DATA_PATH = "src/test/resources/test-data/";

    public JsonReader(String jsonFileName) {
        this.jsonFileName = jsonFileName;
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(Test_DATA_PATH + jsonFileName+".json"));
            jsonReader = data.toJSONString();
            LogsManager.info("JSON file read successfully: ", Test_DATA_PATH, jsonFileName,".json");
        } catch (Exception e) {
            LogsManager.error("Error reading json file: ", Test_DATA_PATH, jsonFileName,".json\n" , e.getMessage());
            jsonReader = "{}"; // Fallback to an empty JSON object in case of error
        }
    }

    /**
     * Reads a specific property from JSON file and returns the value as a String.
     * @return the value for JSON property as a String
     */
    //@Step("Get JSON data by property name: {jsonPropertyName}")
    public String getJsonData(String jsonPropertyName) {
        try {
            LogsManager.info("Reading json data with property_path:", jsonPropertyName);
            return JsonPath.read(jsonReader, jsonPropertyName);
        } catch (Exception e) {
            LogsManager.error("Error reading json data with property_path:" , jsonPropertyName , "\n" , e.getMessage());
            return "";
        }
    }

}