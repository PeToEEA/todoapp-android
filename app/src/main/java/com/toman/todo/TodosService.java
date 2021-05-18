package com.toman.todo;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.toman.todo.dto.TodoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TodosService {

    public void retrieveData(Activity context, ListView listView, Long deletedId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://60a398e57c6e8b0017e27575.mockapi.io/api/todo/todos";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        List<TodoItem> items = mapJsonToItems(response.toString());
                        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, removeDeletedItem(items, deletedId));
                        listView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private List<TodoItem> removeDeletedItem(List<TodoItem> items, Long deletedId) {
        List<TodoItem> todoItems = new ArrayList<>();
        for(TodoItem todoItem : items) {
            if(!todoItem.getId().equals(deletedId)) {
                todoItems.add(todoItem);
            }
        }
        return todoItems;
    }

    private List<TodoItem> mapJsonToItems(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, TodoItem.class);

        List<TodoItem> todoItems = new ArrayList<>();
        try {
            todoItems = objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return todoItems;
    }

    public void saveData(Activity context, TodoItem todoItem) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://60a398e57c6e8b0017e27575.mockapi.io/api/todo/todos";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", todoItem.getId().toString());
            jsonObject.put("objective", todoItem.getObjective());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void deleteData(Activity context, Long id) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://60a398e57c6e8b0017e27575.mockapi.io/api/todo/todos?id=" + id;

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);
    }

}
