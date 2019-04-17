package com.zianderthalapps.a20190415_benjaminstanley_nycschools;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtilities {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = JSONUtilities.class.getSimpleName();

    /**
     * Return a {@link List<School>} object by parsing out information
     * about the schools from the input schoolsJSON string.
     */
    public static List<School> extractSchoolsFromJson(String schoolsJSON) {
        Log.e("Parsing", schoolsJSON);
        ArrayList<School> schools = new ArrayList<>();
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(schoolsJSON)) {
            return null;
        }
        //Catch the JSONException if it's thrown and log it
        try {
            JSONArray schoolJSONArray = new JSONArray(schoolsJSON);
            // If there are results in the features array
            if (schoolJSONArray.length() > 0) {
                for (int i = 0; i < schoolJSONArray.length(); i++) {
                    JSONObject school = schoolJSONArray.getJSONObject(i);
                    // Extract out the title, number of people, and perceived strength values
                    String name = school.getString("school_name");
                    String paragraph = school.getString("overview_paragraph");
                    String dbn = school.getString("dbn");
                    schools.add(new School(name, paragraph, dbn));
                }

                return schools;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return null;
    }

    /**
     * Return a {@link SATScores} object by parsing out information
     * about the school's SAT scores from the input rawJSON string.
     */

    public static SATScores fetchSATScoresFromJSON(String rawJSON){
        SATScores satScores = null;
        try{
            JSONArray sATJSONArray = new JSONArray(rawJSON);
            Log.e("HI", " " + sATJSONArray);
            if(sATJSONArray.length() > 0) {
                JSONObject SATJSON = sATJSONArray.getJSONObject(0);
                String  mathString = SATJSON.getString("sat_math_avg_score");
                String readingString = SATJSON.getString("sat_critical_reading_avg_score");
                String writingString = SATJSON.getString("sat_writing_avg_score");
                //Make sure the Strings are SAT scores and not a placeholder
                //Replace them if they are placeholders to tell the user there is no data
                if(!mathString.matches("-?\\d+")){
                    mathString = "No Data";
                }
                if(!readingString.matches("-?\\d+")){
                    mathString = "No Data";
                }
                if(!writingString.matches("-?\\d+")){
                    mathString = "No Data";
                }
                satScores = new SATScores(mathString, readingString, writingString);
            }
        }catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the JSON results");
        }
        return satScores;
    }
}
