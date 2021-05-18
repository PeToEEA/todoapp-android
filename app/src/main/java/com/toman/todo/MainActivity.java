package com.toman.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toman.todo.dto.TodoItem;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TodosService todosService = new TodosService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.todoList);

        todosService.retrieveData(this, listView, retrieveDeletedId());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Todo.class);
                TodoItem todoItem = (TodoItem) parent.getItemAtPosition(position);
                intent.putExtra("itemId", todoItem.getId());
                intent.putExtra("itemObjective", todoItem.getObjective());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Todo.class);
                startActivity(intent);
            }
        });
    }

    private Long retrieveDeletedId() {
        Intent intent = getIntent();
        if(intent == null || intent.getExtras() == null) {
            return null;
        }
        return intent.getExtras().getLong("deletedId");
    }
}