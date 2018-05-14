package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /*
    * This method gets the Json String and returns the Sandwich bean, it may throw a JsonException
    * */
    public static Sandwich parseSandwichJson(String json) throws JSONException {
        
        //Create final strings with the names of each key inside the Json object
        final String SANDWICH_NAME = "name";
        final String SANDWICH_MAIN_NAME = "mainName";
        final String SANDWICH_ALIAS = "alsoKnownAs";
        final String SANDWICH_PLACE_OF_ORIGIN = "placeOfOrigin";
        final String SANDWICH_DESCRIPTION = "description";
        final String SANDWICH_IMAGE = "image";
        final String SANDWICH_INGREDIENTS = "ingredients";
        
        //Create a new JSON object from the json string parameter
        JSONObject sandwichJson = new JSONObject(json);
        
        //Get the name object which contains the mainName and the alias
        JSONObject sandwichName = sandwichJson.getJSONObject(SANDWICH_NAME);
        
        /*
        * By recommendations use optString() instead of getString(), since it returns a non value even when
        * an empty value is returned by the API
        * */
        
        //Get the mainName from the sandwichName variable
        String mainName = sandwichName.optString(SANDWICH_MAIN_NAME);
        
        //Create a JSON array to get the sandwich alias, since the sandwich can have multiple names
        JSONArray aliasJson = sandwichName.getJSONArray(SANDWICH_ALIAS);
        
        //Get the place of origin from the sandwich Json object
        String placeOfOrigin = sandwichJson.optString(SANDWICH_PLACE_OF_ORIGIN);
        
        //Get the sandwich description from the sandwich Json object
        String description = sandwichJson.optString(SANDWICH_DESCRIPTION);
        
        //Get the sandwich image from the sandwich Json object
        String image = sandwichJson.optString(SANDWICH_IMAGE);
        
        //Get the sandwich list of ingredients from the sandwich Json object
        JSONArray ingredientsJson = sandwichJson.getJSONArray(SANDWICH_INGREDIENTS);
        
        //Create a new Sandwich object so that we set each field from the Json object
        //Return the sandwich object
        return new Sandwich(
                mainName,
                getStringListFromJsonArray(aliasJson),
                placeOfOrigin,
                description,
                image,
                getStringListFromJsonArray(ingredientsJson)
        );
    }
    
    private static List<String> getStringListFromJsonArray(JSONArray jsonArray) throws JSONException {
        //Create a return list so we can add each string from the jsonArray
        List<String> returnList = new ArrayList<>();
        
        //Iterate through each jsonArray field and add it to the returnList
        for (int i = 0; i < jsonArray.length(); i++) {
            String val = jsonArray.getString(i);
            returnList.add(val);
        }
        
        //Return the list when for loop has finished
        return returnList;
    }
}
