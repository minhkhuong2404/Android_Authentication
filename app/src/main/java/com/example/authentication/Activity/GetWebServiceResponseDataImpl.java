package com.example.authentication.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.authentication.Model.Handler.CourseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class GetWebServiceResponseDataImpl implements CourseMVP.GetWebServiceResponseData{
    private StringBuffer response;
    private ArrayList<String> category = new ArrayList<>();
    private Context context;

    public GetWebServiceResponseDataImpl(Context context) {
        this.context = context;
    }

    @Override
    public ArrayList<String> getWebServiceResponseData() {
        try {
            String path = "https://6062ebee0133350017fd227f.mockapi.io/Courses";
            URL url = new URL(path);
            Log.d("REST", "ServerData: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            Log.d("REST", "Response code: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String responseText = response.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.authentication_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("JSON", responseText).apply();

        //Call ServerData() method to call webservice and store result in response
        //  response = service.ServerData(path, postDataParams);
//        Log.d("REST", "data:" + responseText);
        try {
            JSONArray jsonarray = new JSONArray(responseText);
            CourseHandler dbHandler = new CourseHandler(context, null, null, 1);
            dbHandler.deleteAllCourseHandler();

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String CourseCategory = jsonobject.getString("CourseCategory");
                category.add(CourseCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRandomCategory(category);
    }

    @Override
    public ArrayList<String> getRandomCategory(ArrayList<String> categoryList) {
        Collections.shuffle(categoryList);
        Set<String> foo = new HashSet<>(categoryList);
        ArrayList<String> uniqueCategory = new ArrayList<>(foo.size());
        uniqueCategory.addAll(foo);
        Collections.shuffle(uniqueCategory);
        ArrayList<String> ans = new ArrayList<>();
        ans.addAll(uniqueCategory);
        return ans;
    }
}
