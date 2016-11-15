package com.example.myflower;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 14/11/16.
 */

public class Model {
  Integer id;
  String name;
  String url;
  Boolean bookmark;

  @Override
  public String toString() {
    return "Model{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", url='" + url + '\'' +
        '}';
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public Boolean getBookmark() {
    return bookmark;
  }

  public void setBookmark(Boolean bookmark) {
    this.bookmark = bookmark;
  }

  public static List<Model> getListFromJsonString(String JsonString, Context context){
    DBHelper dbHelper = new DBHelper(context);
    ArrayList<String> bookmark = dbHelper.getAllID();

    List<Model> list = new ArrayList<>();
    try {
      JSONObject rootJson = new JSONObject(JsonString);
      JSONArray dataJson = rootJson.getJSONArray("data");

      for(int i =0 ; i< dataJson.length() ; i++){
        JSONObject elementJson = new JSONObject();
        try {
           elementJson = dataJson.getJSONObject(i);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        Model  element = new Model();
        element.setBookmark(false);
        element.setId(elementJson.optInt("id",0));
        if(bookmark.contains(elementJson.optString("id"))){
          element.setBookmark(true);
        }
        element.setName(elementJson.optString("name","Flower Name Missing"));
        element.setUrl(elementJson.optString("url","https://upload.wikimedia.org/wikipedia/commons/7/7e/Aconitum_degenii.jpg"));
        list.add(element);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return list;
  }

}
